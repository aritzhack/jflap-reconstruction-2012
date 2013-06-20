/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */

package model.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import util.JFLAPConstants;
import util.arrows.GeometryHelper;
import model.change.ChangingObject;

/**
 * A graph data structure. The idea behind the graph data structure is that a
 * vertex is just some sort of data structure whose type is not important, and
 * associated with a point. There is therefore no explicit node structure.
 * 
 * @author Thomas Finley
 */

public abstract class Graph<T> extends ChangingObject {

	private int getNextEdgeID() {
		int i = 0;
		Set<Integer> used = new HashSet<Integer>();
		for (Map<T, Integer> map : myEdgeIDs.values()) {
			used.addAll(map.values());
		}
		while (used.contains(i))
			i++;
		return i;
	}

	protected int getID(T from, T to) {
		return myEdgeIDs.get(from).get(to);
	}

	public ControlPoint getControlPt(T from, T to) {
		int edgeID = getID(from, to);
		return myCtrlPoints.get(edgeID);
	}

	public void setControlPt(Point2D ctrl, T from, T to) {
		// Should already be taken care of, there's an
		// issue with geometry when the points are the
		// same and with drawing the points when being updated by the "from"
		// vertex. Still needs to be here due to override in AutomatonDrawer.
	}

	private void applyAutoBend(Point2D ctrl, Point2D pFrom, Point2D pTo) {
		GeometryHelper.translatePerpendicular(ctrl,
				-JFLAPConstants.AUTO_BEND_HEIGHT, pFrom, pTo);
	}

	private void undoAutoBend(T from, T to) {
		GeometryHelper.translatePerpendicular(getControlPt(from, to),
				JFLAPConstants.AUTO_BEND_HEIGHT, pointForVertex(to),
				pointForVertex(from));
	}

	private boolean hasBeenBent(T to, T from) {
		Point2D pFrom = this.pointForVertex(from), pTo = this
				.pointForVertex(to), ctrl = this.getControlPt(from, to), center = GeometryHelper
				.getCenterPoint(pFrom, pTo);
		return !ctrl.equals(center) && !isAutoBent(from, to);
	}

	private boolean isAutoBent(T from, T to) {
		Point2D pFrom = this.pointForVertex(from), pTo = this
				.pointForVertex(to), ctrl = this.getControlPt(from, to), test = GeometryHelper
				.getCenterPoint(pFrom, pTo);
		applyAutoBend(test, pFrom, pTo);
		return test.equals(ctrl);
	}

	private ControlPoint getDefaultControlPoint(T from, T to) {
		Point2D pFrom = this.pointForVertex(from), pTo = this
				.pointForVertex(to), center = GeometryHelper.getCenterPoint(
				pFrom, pTo);
		ControlPoint ctrl = new ControlPoint(center, pFrom, pTo);
		if (from.equals(to)) {
			GeometryHelper.translate(ctrl, Math.PI / 2,
					-JFLAPConstants.INITAL_LOOP_HEIGHT);
		}
		return ctrl;
	}

	private void resetControlPoints() {
		for (T from : myEdgeIDs.keySet()) {
			for (T to : myEdgeIDs.get(from).keySet()) {
				Point2D ctrl = getDefaultControlPoint(from, to);
				setControlPt(ctrl, from, to);
			}
		}
	}

	private Map<T, Map<T, Integer>> myEdgeIDs;
	private Map<Integer, ControlPoint> myCtrlPoints;

	/**
	 * Constructs a directed graph using an automaton.
	 * 
	 * @param automaton
	 *            the automaton to build the graph from
	 */
	public Graph() {
		myEdgeIDs = new TreeMap<T, Map<T, Integer>>();
		myCtrlPoints = new HashMap<Integer, ControlPoint>();
	}

	private Map<T, Point2D> verticesToPoints = new HashMap<T, Point2D>();
	private Map<T, Set<T>> verticesToNeighbors = new HashMap<T, Set<T>>();

	/** Clears all vertices and edges. */
	public void clear() {
		verticesToPoints.clear();
		verticesToNeighbors.clear();
	}

	/** Returns the degree of a vertex. */
	public int degree(T vertex) {
		return adjacent(vertex).size();
	}

	/** Returns the number of vertices. */
	public int numberOfVertices() {
		return verticesToPoints.size();
	}

	/** Returns the set of vertices a vertex is adjacent to. */
	public Set<T> adjacent(T vertex) {
		return (Set<T>) myEdgeIDs.get(vertex).keySet();
	}

	/** Adds an edge between two vertices. */
	public boolean addEdge(T vertex1, T vertex2) {
		Point2D pFrom = this.pointForVertex(vertex1), pTo = this
				.pointForVertex(vertex2);
		int newID = getNextEdgeID();

		// add control point so that autobend can be applied downstream
		ControlPoint ctrl = getDefaultControlPoint(vertex1, vertex2);
		myCtrlPoints.put(newID, ctrl);

		myEdgeIDs.get(vertex1).put(vertex2, newID);
		if (!isDirected())
			myEdgeIDs.get(vertex2).put(vertex1, newID);
		else if (!vertex1.equals(vertex2) && this.hasEdge(vertex2, vertex1)
				&& !hasBeenBent(vertex2, vertex1)) {
			applyAutoBend(ctrl, pFrom, pTo);
			applyAutoBend(getControlPt(vertex2, vertex1), pTo, pFrom);
		}

		distributeChanged();
		return true;
	}

	/** Removes an edge between two vertices. */
	public boolean removeEdge(T vertex1, T vertex2) {
		if (!hasEdge(vertex1, vertex2))
			return false;

		if (!isDirected())
			myEdgeIDs.get(vertex2).remove(vertex1);
		else if (isAutoBent(vertex2, vertex1))
			undoAutoBend(vertex1, vertex2);
		myEdgeIDs.get(vertex1).remove(vertex2);
		myCtrlPoints.remove(getID(vertex1, vertex2));

		distributeChanged();
		return true;
	}

	/** Returns if an edge exists between two vertices. */
	public boolean hasEdge(T vertex1, T vertex2) {
		return adjacent(vertex1).contains(vertex2);
	}

	public boolean hasVertex(T v) {
		return vertices().contains(v);
	}

	/** Adds a vertex. */
	public boolean addVertex(T vertex, Point2D point) {
		if (this.hasVertex(vertex))
			return false;
		myEdgeIDs.put(vertex, new TreeMap<T, Integer>());
		verticesToPoints.put(vertex, (Point2D) point.clone());
		distributeChanged();
		return true;
	}

	/** Removes a vertex. */
	public boolean removeVertex(T vertex) {
		if (this.hasVertex(vertex))
			return false;

		for (Object to : myEdgeIDs.get(vertex).keySet().toArray(new Object[0]))
			removeEdge(vertex, (T) to);

		for (Entry<T, Map<T, Integer>> e : myEdgeIDs.entrySet().toArray(
				new Entry[0])) {
			Map<T, Integer> map = e.getValue();
			if (map.containsKey(vertex))
				removeEdge(e.getKey(), vertex);

		}
		myEdgeIDs.remove(vertex);
		verticesToNeighbors.remove(vertex);
		verticesToPoints.remove(vertex);
		return true;
	}

	/** Moves a vertex to a new point. */
	public void moveVertex(T vertex, Point2D point) {
		this.pointForVertex(vertex).setLocation(point);
		ControlPoint ctrl;
		double x = point.getX(), y = point.getY();

		for (Entry<T, Integer> e : myEdgeIDs.get(vertex).entrySet()) {
			ctrl = myCtrlPoints.get(e.getValue());
			if (vertex.equals(e.getKey())) {
				ctrl.setAll(x, y);
				GeometryHelper.translate(ctrl, Math.PI / 2,
						-JFLAPConstants.INITAL_LOOP_HEIGHT);

			} else
				ctrl.setFrom(x, y);
			setControlPt(ctrl.toBasicPoint(), vertex, e.getKey());

		}
		for (T from : myEdgeIDs.keySet()) {
			Map<T, Integer> map = myEdgeIDs.get(from);
			if (map.containsKey(vertex) && !vertex.equals(from)) {
				int id = map.get(vertex);
				ctrl = myCtrlPoints.get(id);
				ctrl.setTo(x, y);
				setControlPt(ctrl.toBasicPoint(), from, vertex);
			}
		}
		distributeChanged();
	}

	public int totalDegree() {
		int degree = 0;
		for (T v : vertices()) {
			degree += degree(v);
		}
		return degree;
	}

	/** Returns the point for a given vertex. */
	public Point2D pointForVertex(T vertex) {
		return (Point2D) verticesToPoints.get(vertex);
	}

	public T vertexForPoint(Point2D point) {
		for (T vertex : verticesToPoints.keySet())
			if (verticesToPoints.get(vertex).equals(point))
				return vertex;
		return null;
	}

	/**
	 * Returns a copy of the set of vertex objects.
	 * 
	 * @return
	 */
	public Set<T> vertices() {
		return new HashSet<T>(verticesToPoints.keySet());
	}

	/**
	 * Returns the list of vertex points. The order they appear is not
	 * necessarily the same as the vertices.
	 */
	public Point2D[] points() {
		return (Point2D[]) verticesToPoints.values().toArray(new Point2D[0]);
	}

	/** Reforms the points so they are enclosed within a certain frame. */
	public void moveWithinFrame(Rectangle2D bounds) {
		Object[] vertices = vertices().toArray();
		if (vertices.length == 0)
			return;
		Point2D p = pointForVertex((T) vertices[0]);
		double minx = p.getX(), miny = p.getY(), maxx = minx, maxy = miny;
		for (int i = 1; i < vertices.length; i++) {
			p = pointForVertex((T) vertices[i]);
			minx = Math.min(minx, p.getX());
			miny = Math.min(miny, p.getY());
			maxx = Math.max(maxx, p.getX());
			maxy = Math.max(maxy, p.getY());
		}

		minx -= JFLAPConstants.STATE_RADIUS + 5;
		miny -= JFLAPConstants.STATE_RADIUS + 5;
		maxx += JFLAPConstants.STATE_RADIUS + 5;
		maxy += JFLAPConstants.STATE_RADIUS + 5;
		// Now, scale them!
		for (int i = 0; i < vertices.length; i++) {
			p = pointForVertex((T) vertices[i]);
			p = new Point2D.Double((p.getX() - minx) * bounds.getWidth()
					/ (maxx - minx) + bounds.getX(), (p.getY() - miny)
					* bounds.getHeight() / (maxy - miny) + bounds.getY());
			moveVertex((T) vertices[i], p);
		}
	}

	/** Returns a string description of the graph. */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString() + "\n");
		sb.append(verticesToPoints);
		return sb.toString();
	}

	public abstract boolean isDirected();

}
