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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.change.events.AddEvent;
import model.change.events.ModifyEvent;
import model.change.events.RemoveEvent;
import model.graph.layout.GEMLayoutAlgorithm;
import util.JFLAPConstants;
import util.arrows.GeometryHelper;

/**
 * Constructs a transition graph associated with the passed in automaton this
 * graph will automatically update whenever the automaton registers a change.
 * 
 * @author Julian Genkins, Ian McMahon
 * 
 * @param <T>
 */
public class TransitionGraph<T extends Transition<T>> extends
		DirectedGraph<State> implements ChangeListener {

	private Map<Integer, List<T>> myOrderedTransitions;
	private Set<Object> mySelected;
	private Map<T, Point2D> myCenterMap;
	private Automaton<T> myAutomaton;

	public TransitionGraph(Automaton<T> a) {
		this(a, new GEMLayoutAlgorithm());
	}

	public TransitionGraph(Automaton<T> a, LayoutAlgorithm alg) {
		myOrderedTransitions = new HashMap<Integer, List<T>>();
		mySelected = new TreeSet<Object>();
		myCenterMap = new TreeMap<T, Point2D>();
		myAutomaton = a;
		myAutomaton.addListener(this);
		for (State s : a.getStates())
			this.addVertex(s, new Point());
		alg.layout(this, new HashSet<State>());
		for (T t : a.getTransitions()) {
			addTransition(t);
		}
	}

	@Override
	public boolean removeEdge(State from, State to) {
		myOrderedTransitions.remove(getID(from, to));
		boolean needUpdate = hasEdge(to, from) && isAutoBent(to, from);
		boolean remove = super.removeEdge(from, to);
		if (needUpdate)
			updateLabelCenters(to, from);
		return remove;
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		Collection col;
		Iterator it;
		if (event instanceof AddEvent) {
			col = ((AddEvent) event).getToAdd();
			if (col == null || col.isEmpty())
				return;

			it = col.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof State) {
					addVertex((State) o, new Point());
				} else if (o instanceof Transition)
					addTransition((T) o);
			}
		} else if (event instanceof RemoveEvent) {
			col = ((RemoveEvent) event).getToRemove();
			if (col == null || col.isEmpty())
				return;

			it = col.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof State) {
					removeVertex((State) o);
				} else if (o instanceof Transition)
					removeTransition((T) o);
			}
		} else if (event instanceof ModifyEvent) {
			ModifyEvent e = (ModifyEvent) event;
			Object to = e.getArg(1);
			if (to instanceof Transition) {
				updateLabelCenter((T) to);
			}
		}
	}

	@Override
	public void setControlPt(Point2D ctrl, State from, State to) {
		super.setControlPt(ctrl, from, to);
		updateLabelCenters(from, to);
	}

	/** Helper function to simplify control point moving. */
	public void setControlPt(Point2D ctrl, T trans) {
		setControlPt(ctrl, trans.getFromState(), trans.getToState());
	}

	/** Selects or deselects the given object based on <CODE>select</CODE> */
	public boolean setSelected(Object o, boolean select) {
		return select ? mySelected.add(o) : mySelected.remove(o);
	}

	/** Returns true if the given Object is selected. */
	public boolean isSelected(Object o) {
		for (Object sel : mySelected)
			if (sel.equals(o))
				return true;
		return false;
	}

	/**
	 * Returns a List of all transitions from the state <i>from</i> to state
	 * <i>to</i>.
	 */
	public List<T> getOrderedTransitions(State from, State to) {
		return myOrderedTransitions.get(getID(from, to));
	}

	/**
	 * Returns the location of the center point of the label of the given
	 * transition.
	 */
	public Point2D getLabelCenter(T t) {
		return myCenterMap.get(t);
	}

	/** Deselects all objects. */
	public void clearSelection() {
		mySelected.clear();
	}

	/**
	 * Returns the center point for the label specified by the transition based on when it was added (lvl).
	 */
	public Point2D getLabelCenterPoint(T t, int lvl, State from, State to) {
		double d = -(lvl + 1) * JFLAPConstants.EDITOR_CELL_HEIGHT;
		Point2D ctrl = getControlPt(from, to);
		Point2D pFrom = this.pointForVertex(from), pTo = this
				.pointForVertex(to), center = GeometryHelper.getCenterPoint(
				pFrom, pTo);
		center = GeometryHelper.getCenterPoint(center, ctrl);

		if (t.isLoop())
			GeometryHelper.translate(center, Math.PI / 2, d - 5);
		else
			GeometryHelper.translatePerpendicular(center, d, pFrom, pTo);
		return center;
	}

	/**
	 * Adds the transition to the graph, adding an edge if it is the first
	 * transition between the given transition's two states.
	 */
	private void addTransition(T t) {
		State from = t.getFromState();
		State to = t.getToState();

		if (!this.hasEdge(from, to)) {
			boolean changed = addEdge(from, to);
			if (changed)
				myOrderedTransitions.put(getID(from, to), new ArrayList<T>());
		}

		int edgeID = getID(from, to);
		List<T> stack = myOrderedTransitions.get(edgeID);
		stack.add(t);

		updateLabelCenter(t, stack.size() - 1, from, to);

		if (hasEdge(to, from) && isAutoBent(from, to))
			updateLabelCenters(to, from);
	}

	/**
	 * Removes the given transition, removing the edge if it is the last one
	 * between the two states.
	 */
	private void removeTransition(T t) {
		State from = t.getFromState();
		State to = t.getToState();
		if (!this.hasEdge(from, to))
			return;

		int edgeID = getID(from, to);

		// update ordered transitions
		List<T> order = myOrderedTransitions.get(edgeID);
		order.remove(t);

		myCenterMap.remove(t);
		mySelected.remove(t);

		// if there are no more transition to/from these states
		if (order.isEmpty()) {
			removeEdge(from, to);
		} else
			super.distributeChanged();
	}

	/** Updates the label center for the given transition. */
	private void updateLabelCenter(T t) {
		State from = t.getFromState(), to = t.getToState();
		int edgeID = getID(from, to);
		List<T> stack = myOrderedTransitions.get(edgeID);
		int lvl = stack.indexOf(t);
		
		updateLabelCenter(t, lvl, from, to);
	}

	/** Updates all label centers for the edge between from and to. */
	private void updateLabelCenters(State from, State to) {
		int i = 0;
		for (T t : myOrderedTransitions.get(getID(from, to))) {
			updateLabelCenter(t, i++, from, to);
		}
	}

	/** Updates the label center for the given transition based off when it was added (its lvl). */
	private void updateLabelCenter(T t, int lvl, State from, State to) {
		Point2D center = getLabelCenterPoint(t, lvl, from, to);
		myCenterMap.put(t, center);
		distributeChanged();
	}
}
