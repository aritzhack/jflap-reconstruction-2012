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
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.arrows.CurvedArrow;
import util.arrows.CurvedArrowHelper;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.automata.State;
import model.automata.SingleInputTransition;
import model.automata.Transition;


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

	private Automaton<T> myAutomaton;
	private Map<T,String> myLabelToTransitionMap;

	/**
	 * Constructs a directed graph using an automaton.
	 * 
	 * @param automaton
	 *            the automaton to build the graph from
	 */
	public TransitionGraph(Automaton<T> automaton) {
		myAutomaton = automaton;
		myAutomaton.addListener(this);
		myLabelToTransitionMap = new HashMap<T, String>();
		update();
	}

	private void update() {
		this.clear();
		for (State s: myAutomaton.getStates())
			this.addVertex(s, new Point((int)(Math.random()*200),(int) (Math.random()*200)));
		for (T t: myAutomaton.getTransitions()){
			addTransition(t);
		}
	}

	private void addTransition(T t) {
		JFLAPDebug.print(t);
		addEdge(t.getFromState(), t.getToState());
		myLabelToTransitionMap.put(t, t.getLabelText());
	}


	@Override
	public void stateChanged(ChangeEvent arg0) {
		update();
	}

	public Point2D getControlPt(State from, State to) {
		return CurvedArrowHelper.getCenterPoint(pointForVertex(from), pointForVertex(from));
	}

	public List<T> getOrderedTransitions(State from, State to) {
		
		return null;
	}

}
