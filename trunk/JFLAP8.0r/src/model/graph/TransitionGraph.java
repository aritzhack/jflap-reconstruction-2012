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
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	private Map<State,Map<State,Point2D>> myControlPoints;
	private Map<State,Map<State,List<T>>> myOrderedTransitions;

	
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
		myControlPoints= new TreeMap<State, Map<State,Point2D>>();
		myOrderedTransitions = new TreeMap<State, Map<State,List<T>>>();
		for (State s: automaton.getStates())
			this.addVertex(s, new Point());
		a.layout(this, new HashSet<State>());
		for (T t: automaton.getTransitions()){
			addTransition(t);
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
		
		//update ordered transitions
		List<T> order = myOrderedTransitions.get(from).get(to);
		order.remove(t);
		
		myCenterMap.remove(t);
		
		mySelected.remove(t);
		
		//if there are no more transition to/from these states
		if (order.isEmpty()) {
			myOrderedTransitions.get(from).remove(to);
			myControlPoints.get(from).remove(to);
			if (hasEdge(to, from) && isAutoBent(to, from)){
				//undoes the AutoBend
				GeometryHelper.translatePerpendicular(getControlPt(to,from),
						JFLAPConstants.AUTO_BEND_HEIGHT,pointForVertex(to),pointForVertex(from));
			}
			super.removeEdge(from, to);
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
		List<T> stack = myOrderedTransitions.get(from).get(to);
		double d = (stack.size())*JFLAPConstants.EDITOR_CELL_HEIGHT;
		Point2D ctrl =getControlPt(from, to);
		Point2D pFrom=this.pointForVertex(from),
				pTo=this.pointForVertex(to),
				center=GeometryHelper.getCenterPoint(pFrom,pTo);
		center = GeometryHelper.getCenterPoint(center, ctrl);
		JFLAPDebug.print("BEFORE: " + center);
		GeometryHelper.translatePerpendicular(center,d,pFrom,pTo);
		JFLAPDebug.print("AFTER: " + center);

		myCenterMap.put(t, center);
		stack.add(t);
	}


	private void initMappings(State from, State to) {
		Point2D pFrom=this.pointForVertex(from),
				pTo=this.pointForVertex(to),
				ctrl = GeometryHelper.getCenterPoint(pFrom,pTo);
			
		//add control points map
		Map map = myControlPoints.get(from);
		map.put(to, ctrl);
		
		if(from.equals(to)) 
			GeometryHelper.translatePerpendicular(ctrl,
					JFLAPConstants.INITAL_LOOP_HEIGHT,pFrom,pTo);
		//Applies the autobending
		else if (this.hasEdge(to, from) && !hasBeenBent(to,from)){
			applyAutoBend(ctrl,pFrom,pTo);
			applyAutoBend(getControlPt(to, from),pTo,pFrom);
		}

		
		//add transition map
		map = myOrderedTransitions.get(from);
		map.put(to, new ArrayList<T>());
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
			myControlPoints.put(vertex, new TreeMap<State, Point2D>());
			myOrderedTransitions.put(vertex, new TreeMap<State, List<T>>());
		}
		return super.addVertex(vertex, point);
	}

	@Override
	public boolean removeVertex(State vertex) {
		clearMap(myControlPoints,vertex);
		clearMap(myOrderedTransitions,vertex);
		return super.removeVertex(vertex);
	}
	
	private <T> void clearMap(Map<State, Map<State, T>> map, State s) {
		JFLAPDebug.print("CLEARED");
		map.remove(s);
		for (Map<State,?> inside : map.values()){
			inside.remove(s);
		}
	}

	public Point2D getControlPt(State from, State to) {
		return myControlPoints.get(from).get(to);
	}
	
	public boolean setSelected(Object o, boolean select){
		return select ? mySelected.add(o) : mySelected.remove(o);
	}

	public List<T> getOrderedTransitions(State from, State to) {
		return myOrderedTransitions.get(from).get(to);
	}
	
	public Point2D getLabelCenter(T t){
		return myCenterMap.get(t);
	}

}
