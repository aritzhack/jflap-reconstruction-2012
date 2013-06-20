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
import java.io.ObjectInputStream.GetField;
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
import model.graph.layout.GEMLayoutAlgorithm;
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

	

	private Map<Integer,List<T>> myOrderedTransitions;
	private Set<Object> mySelected;
	private Map<T,Point2D> myCenterMap;
	private Automaton<T> myAutomaton;
	public TransitionGraph(Automaton<T> a){
		this(a, new GEMLayoutAlgorithm());
	}

	public TransitionGraph(Automaton<T> a, LayoutAlgorithm alg) {
		myOrderedTransitions = new HashMap<Integer,List<T>>();
		mySelected = new TreeSet<Object>();
		myCenterMap = new TreeMap<T, Point2D>();
		myAutomaton = a;
		myAutomaton.addListener(this);
		for (State s: a.getStates())
			this.addVertex(s, new Point());
		alg.layout(this, new HashSet<State>());
		for (T t: a.getTransitions()){
			addTransition(t);
		}
	}

	@Override
	public boolean addEdge(State from, State to) {
		return myAutomaton.createAndAddTransiton(from, to);
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
	
	public void setControlPt(Point2D ctrl, T trans){
		setControlPt(ctrl, trans.getFromState(), trans.getToState());
	}

	@Override
	public void setControlPt(Point2D ctrl, State from, State to) {
		super.setControlPt(ctrl, from, to);
		updateLabelCenters(from,to);
	}
	
	@Override
	public boolean removeEdge(State from, State to) {
		myOrderedTransitions.remove(getID(from,to));
		return super.removeEdge(from, to);
	}

	private void addTransition(T t) {
		State from = t.getFromState();
		State to = t.getToState();
		if (!this.hasEdge(from, to)) {
			boolean changed = super.addEdge(from, to);
			if (changed) myOrderedTransitions.put(getID(from, to), new ArrayList<T>());
		}

		int edgeID = getID(from, to);
		List<T> stack = myOrderedTransitions.get(edgeID);
		stack.add(t);

		updateLabelCenter(t,stack.size()-1,from,to);


	}
	
	private void updateLabelCenters(State from, State to) {
		int i = 0;
		for (T t: myOrderedTransitions.get(getID(from, to))){
			updateLabelCenter(t,i++,from,to);
		}
	}
	private void updateLabelCenter(T t, int lvl, State from, State to) {
		double d = -(lvl+1)*JFLAPConstants.EDITOR_CELL_HEIGHT;
		Point2D ctrl =getControlPt(from, to);
		Point2D pFrom=this.pointForVertex(from),
				pTo=this.pointForVertex(to),
				center=GeometryHelper.getCenterPoint(pFrom,pTo);
		center = GeometryHelper.getCenterPoint(center, ctrl);

		if (t.isLoop()) GeometryHelper.translate(center,Math.PI/2,d-5);
		else GeometryHelper.translatePerpendicular(center,d,pFrom,pTo);

		myCenterMap.put(t, center);		
	}

	private void updateLabelCenter(T t) {
		State from=t.getFromState(), to=t.getToState();
		int edgeID = getID(from, to);
		List<T> stack = myOrderedTransitions.get(edgeID);
		int lvl = stack.indexOf(t);
		updateLabelCenter(t, lvl, from, to);
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


	public boolean setSelected(Object o, boolean select){
		return select ? mySelected.add(o) : mySelected.remove(o);
	}
	
	public boolean isSelected(Object o){
		return mySelected.contains(o);
	}

	public List<T> getOrderedTransitions(State from, State to) {
		return myOrderedTransitions.get(getID(from, to));
	}

	public Point2D getLabelCenter(T t){
		return myCenterMap.get(t);
	}

	public void clearSelection() {
		mySelected.clear();
	}
}
