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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.event.ChangeEvent;

import model.change.ChangingObject;

/**
 * A graph data structure. The idea behind the graph data structure is that a
 * vertex is just some sort of data structure whose type is not important, and
 * associated with a point. There is therefore no explicit node structure.
 * 
 * @author Thomas Finley
 */

public class Graph<T> extends ChangingObject{
	/** Creates a new empty graph structure. */
	public Graph() {

	}

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
		if (!verticesToNeighbors.containsKey(vertex))
			verticesToNeighbors.put(vertex, new HashSet<T>());
		return (Set<T>) verticesToNeighbors.get(vertex);
	}

	/** Adds an edge between two vertices. */
	public boolean addEdge(T vertex1, T vertex2) {
		boolean changed = adjacent(vertex1).add(vertex2) &&
					adjacent(vertex2).add(vertex1);
		if (changed) distributeChanged();
		return changed;
	}

	/** Removes an edge between two vertices. */
	public boolean removeEdge(T vertex1, T vertex2) {
		boolean changed = adjacent(vertex1).remove(vertex2) &&
				adjacent(vertex2).remove(vertex1);
		if (changed) distributeChanged();
		return changed;
	}

	/** Returns if an edge exists between two vertices. */
	public boolean hasEdge(T vertex1, T vertex2) {
		return adjacent(vertex1).contains(vertex2);
	}

	public boolean hasVertex(T v){
		return vertices().contains(v);
	}
	
	/** Adds a vertex. */
	public boolean addVertex(T vertex, Point2D point) {
		boolean changed = verticesToPoints.put(vertex, (Point2D) point.clone()) != null;
		if (changed) distributeChanged();
		return changed;
	}

	/** Removes a vertex. */
	public boolean removeVertex(T vertex) {
		Set<T> others = adjacent(vertex);
		Iterator<T> it = others.iterator();
		while (it.hasNext())
			adjacent(it.next()).remove(vertex);
		boolean changed = verticesToNeighbors.remove(vertex) != null &&
				verticesToPoints.remove(vertex) != null;
		if (changed) distributeChanged();
		return changed; 
	}

	/** Moves a vertex to a new point. */
	public void moveVertex(T vertex, Point2D point) {
		addVertex(vertex, point);
		distributeChanged();
	}

	public int totalDegree(){
		int degree = 0;
		for (T v: vertices()){
			degree += degree(v);
		}
		return degree;
	}
	/** Returns the point for a given vertex. */
	public Point2D pointForVertex(T vertex) {
		return (Point2D) verticesToPoints.get(vertex);
	}

	/**
	 * Returns a copy of the set of vertex objects.
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
		Point2D p = pointForVertex((T)vertices[0]);
		double minx = p.getX(), miny = p.getY(), maxx = minx, maxy = miny;
		for (int i = 1; i < vertices.length; i++) {
			p = pointForVertex((T) vertices[i]);
			minx = Math.min(minx, p.getX());
			miny = Math.min(miny, p.getY());
			maxx = Math.max(maxx, p.getX());
			maxy = Math.max(maxy, p.getY());
		}
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

	private Map<T, Point2D> verticesToPoints = new HashMap<T, Point2D>();

	private Map<T, Set<T>> verticesToNeighbors = new HashMap<T, Set<T>>();
}
