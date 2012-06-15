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
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.automata.Automaton;
import model.automata.State;
import model.automata.SingleInputTransition;


/**
 * Constructs a transition graph associated with the passed in automaton
 * this graph will automatically update whenever the automaton registers
 * a change.
 * 
 * @author Julian Genkins
 *
 * @param <T>
 */
public class TransitionGraph<T extends SingleInputTransition<T>> extends DirectedGraph<State> 
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
		for (T t: myAutomaton.getTransitions()){
			addTransition(t);
		}
	}

	private void addTransition(T t) {
		addEdge(t.getFromState(), t.getToState());
		myLabelToTransitionMap.put(t, t.getLabelText());
	}


	@Override
	public void stateChanged(ChangeEvent arg0) {
		update();
	}

}
