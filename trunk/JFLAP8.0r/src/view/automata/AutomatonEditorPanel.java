package view.automata;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.automata.Automaton;
import model.automata.AutomatonException;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.automata.acceptors.Acceptor;
import model.change.events.RemoveEvent;
import model.change.events.StartStateSetEvent;
import model.graph.ControlPoint;
import model.graph.TransitionGraph;
import model.undo.IUndoRedo;
import model.undo.UndoKeeper;
import util.JFLAPConstants;
import util.arrows.CurvedArrow;
import util.view.GraphHelper;
import view.EditingPanel;
import view.automata.tools.DeleteTool;
import view.automata.tools.EditingTool;
import view.automata.tools.Tool;
import view.automata.tools.ToolListener;
import view.automata.transitiontable.TransitionTable;
import view.automata.transitiontable.TransitionTableFactory;
import debug.JFLAPDebug;

public class AutomatonEditorPanel<T extends Automaton<S>, S extends Transition<S>>
		extends EditingPanel implements ToolListener, ChangeListener {

	private EditingTool<T, S> myTool;
	private T myAutomaton;
	private TransitionGraph<S> myGraph;
	private SelectionAutomatonDrawer<S> myDrawer;
	private AffineTransform transform;
	private List<Note> myNotes;

	public AutomatonEditorPanel(T m, UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		myAutomaton = m;
		myGraph = new TransitionGraph<S>(m);
		myGraph.addListener(this);
		StateDrawer vDraw = new StateDrawer();
		myDrawer = new SelectionAutomatonDrawer<S>(vDraw);
		transform = new AffineTransform();
		myNotes = new ArrayList<Note>();
		addKeyListener(new DeleteKeyListener());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		updateBounds(g);

		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		setBackground(java.awt.Color.white);

		Graphics2D g2 = (Graphics2D) g;
		g2.transform(transform);
		myDrawer.draw(myGraph, g2);

		for(Note n : myNotes)
			n.draw(g2);

		if (myTool != null)
			myTool.draw(g2);
	}

	@Override
	public void setMagnification(double mag) {
		// TODO: Resize automata & notes, may actually want separate slider for
		// automaton. Issue with changing magnification and points.
		// transform.setToScale(mag * 2, mag * 2);
		super.setMagnification(mag);
		repaint();
	}

	@Override
	public void toolActivated(Tool e) {
		setTool((EditingTool) e);
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		revalidate();
		repaint();
	}

	public void stopAllEditing() {
		// TODO: needs to be implemented so undo/redo doesn't create crazy
		// graphics with the TransitionTables
		// Also so you don't break things (like undoing a creation while you
		// still have the state selected)
		JFLAPDebug.print("NEEDS TO BE IMPLEMENTED!");
	}

	/**
	 * Returns the State, Transition, Arrow (in the form of a State[]), or Note
	 * at the given point, or null if there is none of them.
	 */
	public Object objectAtPoint(Point2D p) {
		Object o = stateAtPoint(p);
		if (o == null)
			o = transitionAtPoint(p);
		if (o == null)
			o = arrowAtPoint(p);
		if (o == null)
			o = noteAtPoint(p);
		return o;
	}

	/**
	 * Removes the old tool and sets the new tool to listen for mouse and mouse
	 * motion events. Also will update the cursor (to an X if it is a delete
	 * tool, or a simple arrow otherwise).
	 */
	public void setTool(EditingTool<T, S> t) {
		if(myTool != null)
			myTool.setActive(false);
		myTool = t;
		myTool.setActive(true);
	}

	/** Sets the given object as selected in the AutomatonDrawer. */
	public void selectObject(Object o) {
		myDrawer.setSelected(o, true);
		repaint();
	}
	
	public void selectAll(Collection<? extends Object> objs){
		for(Object o : objs){
			myDrawer.setSelected(o, true);
		}
		repaint();
	}
	
	public List<Object> selectAllInBounds(Rectangle bounds){
		Set<S> tranSet = new TreeSet<S>();
		Set<State> stateSet = new TreeSet<State>();
		Set<State[]> edgeSet = new HashSet<State[]>();
		
		for(State vertex : myAutomaton.getStates()){
			Point2D current = myGraph.pointForVertex(vertex);
			if(bounds.contains(current))
				stateSet.add(vertex);
		}
		
		for(S trans : myAutomaton.getTransitions()){
			LabelBounds label = GraphHelper.getLabelBounds(myGraph, trans, getGraphics());
			State from = trans.getFromState(), to = trans.getToState();
			CurvedArrow arrow = myDrawer.getArrow(from, to, myGraph);
			
			if(bounds.intersects(label.getRectangle()) || bounds.contains(label.getRectangle()))
				tranSet.add(trans);
			if(arrow.intersects(bounds)){
				tranSet.addAll(myGraph.getOrderedTransitions(from, to));
				edgeSet.add(new State[]{from, to});
			}
		}
		List<Object> list = new ArrayList<Object>(tranSet);
		list.addAll(stateSet);
		list.addAll(edgeSet);
		return list;
	}

	/** Removes all selected objects in the AutomatonDrawer. */
	public void clearSelection() {
		myDrawer.clearSelection();
		repaint();
	}

	// public void showMenu(Point2D p) {
	// Object o = objectAtPoint(p);
	// }

	/**
	 * Creates and adds a new, default-named state to the automaton, and sets
	 * its location to the given point.
	 */
	public State createState(Point point) {
		StateSet states = myAutomaton.getStates();
		State vertex = states.createAndAddState();
		myGraph.moveVertex(vertex, point);
		return vertex;
	}

	/** Moves the state to the specified point. */
	public void moveState(State s, Point2D p) {
		myGraph.moveVertex(s, p);
	}

	/**
	 * Deletes the given state from the Automaton, and notifies the UndoKeeper
	 * to allow for a compound event where all transitions to or from vertex
	 * will also be added or deleted.
	 */
	public void removeState(State vertex) {
		Point2D p = (Point2D) getPointForVertex(vertex).clone();
		TransitionSet<S> transitions = myAutomaton.getTransitions();

		Set<S> transFromOrTo = transitions.getTransitionsFromState(vertex);
		transFromOrTo.addAll(transitions.getTransitionsToState(vertex));

		getKeeper().applyAndListen(
				new StateAndTransRemoveEvent(new State[] { vertex },
						transFromOrTo, new Point2D[] { p }));
	}

	/** Returns a the location of vertex in the TransitionGraph. */
	public Point2D getPointForVertex(State vertex) {
		return myGraph.pointForVertex(vertex);
	}

	/**
	 * Initializes a blank transition to be edited prior to being added to the
	 * Automaton.
	 */
	public S createTransition(State from, State to) {
		StateSet states = myAutomaton.getStates();
		if (!(states.contains(from) && states.contains(to)))
			return null;

		return myAutomaton.createBlankTransition(from, to);
	}

	/**
	 * Initializes an editing table for the specified transition at the location
	 * of the transition's label center. If the transition is brand new (ie. not
	 * in the Automaton at the point this is called), the label center will be
	 * calculated based on default settings.
	 */
	public void editTransition(S trans, boolean isNew) {
		TransitionTable<T, S> table = TransitionTableFactory.createTable(trans,
				myAutomaton, this);
		table.setCellSelectionEnabled(true);
		table.changeSelection(0, 0, false, false);

		final Dimension tableSize = table.getSize();
		Point2D center = isNew ? GraphHelper.calculateCenterPoint(myGraph,
				trans) : myGraph.getLabelCenter(trans);
		final Point tablePoint = new Point((int) center.getX()
				- tableSize.width / 2, (int) center.getY() - tableSize.height
				/ 2);

		table.setSize(tableSize);
		table.setLocation(tablePoint);

		table.addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent e) {
			}

			public void componentMoved(ComponentEvent e) {
				e.getComponent().setLocation(tablePoint);
			}

			public void componentResized(ComponentEvent e) {
				e.getComponent().setSize(tableSize);
			}

			public void componentShown(ComponentEvent e) {
			}
		});
		table.requestFocus();
	}

	/** Removes the transition from the Automaton and notifies the UndoKeeper. */
	public void removeTransition(S trans) {
		getKeeper().applyAndListen(new TransitionRemoveEvent(trans));
	}

	/**
	 * Returns the location of the ControlPoint for the edge from states[0] to
	 * state[1].
	 */
	public Point2D getControlPoint(State[] states) {
		return myGraph.getControlPt(states[0], states[1]).toBasicPoint();
	}

	/**
	 * Moves the ControlPoint for the edge between from and to to the specified
	 * point.
	 */
	public void moveCtrlPoint(State from, State to, Point2D point) {
		ControlPoint ctrl = myGraph.getControlPt(from, to);
		ctrl.setLocation(point);
		myGraph.setControlPt(ctrl, from, to);
	}

	/**
	 * Removes an edge and all transitions from <i>from</i> to <i>to</i> and
	 * notifies the UndoKeeper.
	 */
	public void removeEdge(State from, State to) {
		S[] temp = (S[]) myGraph.getOrderedTransitions(from, to).toArray(
				new Transition[0]);

		getKeeper().applyAndListen(
				new TransitionRemoveEvent(temp));
	}


	public void createAndAddNote(Point p) {
		Note note = new Note(this, p, "need to figure this out!");
		myNotes.add(note);
		editNote(note);
	}

	public void editNote(Note n) {
		//TODO: Notes?
		repaint();
	}

	/**
	 * Returns the state at the specified point, if one exists. If there are
	 * multiple, it will return the most recently created one (which is rendered
	 * as "on top" as well).
	 */
	private State stateAtPoint(Point2D p) {
		Point2D[] points = myGraph.points();
		for (int i = points.length - 1; i >= 0; i--) {
			if (p.distance(points[i]) <= getStateRadius())
				return myGraph.vertexForPoint(points[i]);
		}
		return null;
	}

	/**
	 * Returns the transition at the given point, which is calculated by seeing
	 * if the point is within the label bounds of that transition.
	 */
	private S transitionAtPoint(Point2D p) {
		for (S trans : myAutomaton.getTransitions()) {
			LabelBounds bounds = GraphHelper.getLabelBounds(myGraph, trans,
					getGraphics());
			bounds = new LabelBounds(-bounds.getAngle(), bounds.getRectangle());
			if (bounds.contains(p))
				return trans;
		}
		return null;
	}

	/**
	 * Returns the arrow at the specified point, if one exists, by checking if
	 * there is an edge that intersects that point.
	 */
	private State[] arrowAtPoint(Point2D p) {
		for (S trans : myAutomaton.getTransitions()) {
			State from = trans.getFromState(), to = trans.getToState();

			CurvedArrow edge = myDrawer.getArrow(from, to, myGraph);
			if (CurvedArrow.intersects(p, 2, edge))
				return new State[] { from, to };
		}
		return null;
	}

	private Note noteAtPoint(Point2D p) {
		// TODO Notes need to be implemented
		return null;
	}

	/**
	 * Updates the graph so that all parts (States, arrows, labels, notes) are
	 * moved to an accessible region (within the enclosing scroll pane), and the
	 * preferred size contains the entire graph (so the containing scroll pane
	 * knows to resize).
	 */
	private void updateBounds(Graphics g) {
		double maxx = 0, maxy = 0, minx = getStateBounds(), miny = getStateBounds();

		// Compare to State points
		for (State vert : myAutomaton.getStates()) {
			Point2D point = myGraph.pointForVertex(vert);
			double x = point.getX(), y = point.getY();

			maxx = Math.max(maxx, x);
			maxy = Math.max(maxy, y);
			miny = Math.min(miny, y);
			if (vert.equals(myAutomaton.getStartState()))
				x -= JFLAPConstants.STATE_RADIUS;
			minx = Math.min(minx, x);
		}

		// Compare to labels and arrow boundaries
		for (S trans : myAutomaton.getTransitions()) {
			LabelBounds labelBounds = GraphHelper.getLabelBounds(myGraph,
					trans, g);
			CurvedArrow arrow = myDrawer.getArrow(trans.getFromState(),
					trans.getToState(), myGraph);
			Rectangle2D arrowBounds = arrow.getCurveBounds();

			maxx = Math.max(maxx, labelBounds.getMaxX());
			maxx = Math.max(maxx, arrowBounds.getMaxX());

			minx = Math.min(minx, labelBounds.getMinX());
			minx = Math.min(minx, arrowBounds.getMinX());

			maxy = Math.max(maxy, labelBounds.getMaxY());
			maxy = Math.max(maxy, arrowBounds.getMaxY());

			miny = Math.min(miny, labelBounds.getMinY());
			miny = Math.min(miny, arrowBounds.getMinY());
		}

		// TODO: Notes, when they're implemented

		maxx += getStateBounds();
		maxy += getStateBounds();
		minx -= getStateBounds();
		miny -= getStateBounds();

		int x = (int) Math.ceil(maxx), y = (int) Math.ceil(maxy);
		setPreferredSize(new Dimension(x, y));

		if (minx < 0 || miny < 0) {
			// Adjust so they get off the boundary
			minx -= minx < 0 ? 1 : 0;
			miny -= miny < 0 ? 1 : 0;

			// We must adjust all the states so that everything is viewable.
			for (State vert : myAutomaton.getStates()) {
				Point2D current = myGraph.pointForVertex(vert);

				myGraph.moveVertex(vert, new Point2D.Double(current.getX()
						- minx, current.getY() - miny));

				for (State adj : myGraph.adjacent(vert)) {
					Point2D ctrl = myGraph.getControlPt(vert, adj);

					if (ctrl.getX() < 0 || ctrl.getY() < 0)
						moveCtrlPoint(vert, adj, new Point2D.Double(ctrl.getX()
								- minx, ctrl.getY() - miny));
				}
			}
		}
	}

	/**
	 * Returns the radius of the vertex drawer.
	 */
	private double getStateRadius() {
		return myDrawer.getVertexDrawer().getVertexRadius();
	}

	/**
	 * Returns the radius of the vertex drawer with some additional padding for
	 * bounds.
	 */
	private double getStateBounds() {
		return getStateRadius() + 5;
	}

	private class DeleteKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_DELETE) {
				stopAllEditing();
				State[] states = myDrawer.getSelectedStates().toArray(
						new State[0]);
				Set<S> trans = new TreeSet<S>(myDrawer.getSelectedTransitions());
				myDrawer.clearSelection();

				TransitionSet<S> transitionSet = myAutomaton.getTransitions();
				UndoKeeper keeper = getKeeper();

				if (states.length > 0) {
					Point2D[] points = new Point2D[states.length];
					for (int i = 0; i < states.length; i++) {
						points[i] = GraphHelper.getOnscreenPoint(
								Automaton.isStartState(myAutomaton, states[i]),
								myGraph.pointForVertex(states[i]));

						trans.addAll(transitionSet
								.getTransitionsFromState(states[i]));
						trans.addAll(transitionSet
								.getTransitionsToState(states[i]));
					}
					keeper.applyAndListen(new StateAndTransRemoveEvent(states,
							trans, points));
				} else
					getKeeper().applyAndListen(
							new TransitionRemoveEvent(trans));
			}
		}
	}

	/**
	 * Event for undoing the deletion of a state and transitions.
	 */
	private class StateAndTransRemoveEvent implements IUndoRedo {

		private State[] myStates;
		private Point2D[] myPoints;
		private List<IUndoRedo> myEvents;

		public StateAndTransRemoveEvent(State[] states, Set<S> transitions,
				Point2D[] points) {
			if (states.length != points.length) {
				throw new AutomatonException("Error with State Deletion");
			}
			myStates = states;
			myPoints = points;
			myEvents = new ArrayList<IUndoRedo>();

			myEvents.add(new RemoveEvent<State>(myAutomaton.getStates(), states));
			for (State s : myStates) {
				if (Automaton.isStartState(myAutomaton, s))
					myEvents.add(new StartStateSetEvent(myAutomaton
							.getComponentOfClass(StartState.class), s, null));
				if (myAutomaton instanceof Acceptor
						&& Acceptor.isFinalState((Acceptor) myAutomaton, s))
					myEvents.add(new RemoveEvent<State>(
							((Acceptor) myAutomaton).getFinalStateSet(), s));
			}
			if (!transitions.isEmpty())
				myEvents.add(new TransitionRemoveEvent(transitions));
		}

		@Override
		public boolean undo() {
			boolean allUndone = true;
			for (int i = 0; i < myEvents.size(); i++) {
				if (!myEvents.get(i).undo())
					allUndone = false;
				// Move all vertexes to their original positions before messing
				// with them.
				if (i == 0)
					for (int j = 0; j < myStates.length; j++)
						myGraph.moveVertex(myStates[j], myPoints[j]);
			}
			clearSelection();
			return allUndone;
		}

		@Override
		public boolean redo() {
			boolean allRedone = true;
			for (int i = myEvents.size() - 1; i >= 0; i--)
				if (!myEvents.get(i).redo())
					allRedone = false;
			clearSelection();
			return allRedone;
		}

		@Override
		public String getName() {
			return "Remove State and all transitions";
		}
	}

	private class TransitionRemoveEvent extends RemoveEvent<S> {

		Point2D[] myPoints;

		public TransitionRemoveEvent(Collection<S> transitions) {
			this((S[]) transitions.toArray(new Transition[0]));
		}

		public TransitionRemoveEvent(S... transitions) {
			super(myAutomaton.getTransitions(), transitions);
			myPoints = new Point2D[transitions.length];

			for (int i = 0; i < transitions.length; i++) {
				myPoints[i] = myGraph.getControlPt(
						transitions[i].getFromState(),
						transitions[i].getToState());
			}
		}
		
		@Override
		public boolean redo() {
			clearSelection();
			return super.redo();
		}

		@Override
		public boolean undo() {
			boolean undo = super.undo();
			int i = 0;
			for (S trans : getToRemove()) {
				moveCtrlPoint(trans.getFromState(), trans.getToState(),
						myPoints[i]);
				i++;
			}
			clearSelection();
			return undo;
		}
	}
}
