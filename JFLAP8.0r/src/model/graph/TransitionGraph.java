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

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.JFLAPConstants;
import util.arrows.CurvedArrow;
import util.arrows.GeometryHelper;
import view.automata.LabelBounds;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.automata.State;
import model.automata.SingleInputTransition;
import model.automata.Transition;
import model.change.events.AddEvent;
import model.change.events.AdvancedChangeEvent;
import model.change.events.RemoveEvent;
import model.graph.layout.CircleLayoutAlgorithm;
import model.graph.layout.SpiralLayoutAlgorithm;


/**
 * Constructs a transition graph associated with the passed in automaton
 * this graph will automatically update whenever the automaton registers
 * a change.
 * 
 * @author Julian Genkins
 *
 * @param <T>
 */
public class TransitionGraph<T extends Transition<T>> extends DirectedGraph<State> 
implements ChangeListener {	

	private Set<Object> mySelected;
	private Map<T,Point2D> myCenterMap;
	private Map<State,Map<State,Integer>> myEdgeIDs;
	private Map<Integer,ControlPoint> myCtrlPoints;
	private Map<Integer,List<T>> myOrderedTransitions;


	public TransitionGraph(Automaton<T> a){
		this(a, new CircleLayoutAlgorithm());
	}

	/**
	 * Constructs a directed graph using an automaton.
	 * 
	 * @param automaton
	 *            the automaton to build the graph from
	 */
	public TransitionGraph(Automaton<T> automaton, LayoutAlgorithm a) {
		automaton.addListener(this);
		mySelected = new TreeSet<Object>();
		myCenterMap = new TreeMap<T, Point2D>();
		myEdgeIDs = new TreeMap<State, Map<State,Integer>>();
		myCtrlPoints = new HashMap<Integer, ControlPoint>();
		myOrderedTransitions = new HashMap<Integer,List<T>>();
		for (State s: automaton.getStates())
			this.addVertex(s, new Point());
		a.layout(this, new HashSet<State>());
		for (T t: automaton.getTransitions()){
			addTransition(t);
		}
		resetControlPoints();
	}


	private void resetControlPoints() {
		for (State from: myEdgeIDs.keySet()){
			for (State to: myEdgeIDs.get(from).keySet()){
				Point2D ctrl = getDefaultControlPoint(from, to);
				setControlPt(ctrl, from, to);
			}
		}
	}

	@Override @Deprecated
	public boolean addEdge(State from, State to) {
		//DO NOT CALL THIS METHOD In TransitionGraph
		//instead add a transition to the automaton within.
		return false;
	}

	private void removeTransition(T t) {
		State from = t.getFromState();
		State to = t.getToState();
		if (!this.hasEdge(from, to)) return;

		int edgeID = getID(from, to);

		//update ordered transitions
		List<T> order = myOrderedTransitions.get(edgeID);
		order.remove(t);

		myCenterMap.remove(t);

		mySelected.remove(t);

		//if there are no more transition to/from these states
		if (order.isEmpty()) {
			this.removeEdge(from,to);
		}
		else
			super.distributeChanged();

	}

	private void addTransition(T t) {
		State from = t.getFromState();
		State to = t.getToState();
		if (!this.hasEdge(from, to)){
			super.addEdge(from, to);
			initMappings(from, to);
		}

		int edgeID = getID(from, to);
		List<T> stack = myOrderedTransitions.get(edgeID);
		double d = (stack.size())*JFLAPConstants.EDITOR_CELL_HEIGHT;
		Point2D ctrl =getControlPt(from, to);
		Point2D pFrom=this.pointForVertex(from),
				pTo=this.pointForVertex(to),
				center=GeometryHelper.getCenterPoint(pFrom,pTo);
		center = GeometryHelper.getCenterPoint(center, ctrl);

		GeometryHelper.translatePerpendicular(center,d,pFrom,pTo);

		myCenterMap.put(t, center);
		stack.add(t);
	}


	private void initMappings(State from, State to) {
		Point2D pFrom=this.pointForVertex(from),
				pTo=this.pointForVertex(to);

		ControlPoint ctrl = getDefaultControlPoint(from,to);
		
		//add control points map
		int newID = getNextEdgeID();

		Map<State,Integer> mapping = myEdgeIDs.get(from);
		mapping.put(to, newID);
		myCtrlPoints.put(newID, ctrl);
		
		if(from.equals(to)) 
			GeometryHelper.translatePerpendicular(ctrl,
					JFLAPConstants.INITAL_LOOP_HEIGHT,pFrom,pTo);
		//Applies the autobending
		else if (this.hasEdge(to, from) && !hasBeenBent(to,from)){
			applyAutoBend(ctrl,pFrom,pTo);
			applyAutoBend(myCtrlPoints.get(getID(to, from)),pTo,pFrom);
		}

		JFLAPDebug.print(myEdgeIDs);

		//add transition map
		myOrderedTransitions.put(newID, new ArrayList<T>());
	}


	private ControlPoint getDefaultControlPoint(State from, State to) {
		Point2D pFrom=this.pointForVertex(from),
				pTo=this.pointForVertex(to),
				center = GeometryHelper.getCenterPoint(pFrom,pTo);
		JFLAPDebug.print("From: "+pFrom+"\tCenter: "+center+"\tTo: "+pTo);
		return new ControlPoint(center,pFrom,pTo);
	}

	private int getNextEdgeID() {
		int i=0;
		Set<Integer> used = new HashSet<Integer>();
		for (Map<State,Integer> map: myEdgeIDs.values()){
			used.addAll(map.values());
		}
		while (used.contains(i)) i++;
		return i;
	}

	private int getID(State from, State to){
		return myEdgeIDs.get(from).get(to);
	}

	private void applyAutoBend(Point2D ctrl, Point2D pFrom, Point2D pTo) {
		GeometryHelper.translatePerpendicular(ctrl,
				JFLAPConstants.AUTO_BEND_HEIGHT,pTo,pFrom);

	}

	private boolean hasBeenBent(State to, State from) {
		Point2D pFrom=this.pointForVertex(from),
				pTo=this.pointForVertex(to),
				ctrl=this.getControlPt(from, to),
				center=GeometryHelper.getCenterPoint(pFrom,pTo);
		return !ctrl.equals(center) && !isAutoBent(from,to);
	}

	private boolean isAutoBent(State from, State to) {
		Point2D pFrom=this.pointForVertex(from),
				pTo=this.pointForVertex(to),
				ctrl=this.getControlPt(from, to),
				test=GeometryHelper.getCenterPoint(pFrom,pTo);
		applyAutoBend(test, pFrom, pTo);
		return test.equals(ctrl);
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		if (event instanceof AddEvent){
			Object o = ((AddEvent) event).getToAdd();
			if (o instanceof State){
				addVertex((State) o, new Point());
			}
			else if (o instanceof Transition)
				addTransition((T) o);
		}
		else if (event instanceof RemoveEvent){
			Object o = ((RemoveEvent) event).getToRemove();
			if (o instanceof State){
				removeVertex((State) o);
			}
			else if (o instanceof Transition)
				removeTransition((T) o);
		}
	}


	@Override
	public boolean addVertex(State vertex, Point2D point) {
		if (!this.hasVertex(vertex)){
			myEdgeIDs.put(vertex, new TreeMap<State, Integer>());
		}
		return super.addVertex(vertex, point);
	}

	@Override
	public boolean removeVertex(State vertex) {
		for (State to: myEdgeIDs.get(vertex).keySet().toArray(new State[0])){
			removeEdge(vertex,to);
		}
		myEdgeIDs.remove(vertex);
		for (Entry<State,Map<State,Integer>> e: myEdgeIDs.entrySet().toArray(new Entry[0])){
			Map<State,Integer> map = e.getValue();
			if (map.containsKey(vertex))
				removeEdge(e.getKey(), vertex);

		}

		return super.removeVertex(vertex);
	}

	@Override
	public boolean removeEdge(State from, State to) {
		int edgeID = getID(from, to);
		myOrderedTransitions.remove(edgeID);
		myCtrlPoints.remove(edgeID);
		if (hasEdge(to, from) && isAutoBent(to, from)){
			//undoes the AutoBend
			GeometryHelper.translatePerpendicular(getControlPt(to,from),
					JFLAPConstants.AUTO_BEND_HEIGHT,pointForVertex(to),pointForVertex(from));
		}
		myEdgeIDs.get(from).remove(to);
		return super.removeEdge(from, to);
	}


	@Override
	public void moveVertex(State vertex, Point2D point) {
		super.moveVertex(vertex, point);
		for (int id: myEdgeIDs.get(vertex).values()){
			myCtrlPoints.get(id).setFrom(point.getX(), point.getY());
		}
		for (Map<State,Integer> map: myEdgeIDs.values()){
			if (map.containsKey(vertex)){
				int id=map.get(vertex);
				myCtrlPoints.get(id).setTo(point.getX(), point.getY());
			}
		}
	}

	public Point2D getControlPt(State from, State to) {
		int edgeID = getID(from, to);
		return myCtrlPoints.get(edgeID).toBasicPoint();
	}


	public void setControlPt(Point2D ctrl, T trans){
		setControlPt(ctrl, trans.getFromState(), trans.getToState());
	}

	public void setControlPt(Point2D ctrl, State from, State to) {
		int id = getID(from, to);
		myCtrlPoints.get(id).setTo(ctrl.getX(), ctrl.getY());
	}

	public boolean setSelected(Object o, boolean select){
		return select ? mySelected.add(o) : mySelected.remove(o);
	}

	public List<T> getOrderedTransitions(State from, State to) {
		return myOrderedTransitions.get(getID(from, to));
	}

	public Point2D getLabelCenter(T t){
		return myCenterMap.get(t);
	}

}
