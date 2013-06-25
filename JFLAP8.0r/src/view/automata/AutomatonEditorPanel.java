package view.automata;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.automata.State;
import model.automata.StateSet;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.change.events.AddEvent;
import model.change.events.RemoveEvent;
import model.graph.ControlPoint;
import model.graph.TransitionGraph;
import model.undo.UndoKeeper;
import util.JFLAPConstants;
import util.arrows.CurvedArrow;
import util.arrows.GeometryHelper;
import view.EditingPanel;
import view.automata.tools.DeleteTool;
import view.automata.tools.Tool;
import view.automata.tools.ToolListener;
import view.automata.transitiontable.TransitionTable;
import view.automata.transitiontable.TransitionTableFactory;

public class AutomatonEditorPanel<T extends Automaton<S>, S extends Transition<S>>
		extends EditingPanel implements ToolListener, ChangeListener {

	private Tool myTool;
	private T myAutomaton;
	private TransitionGraph<S> myGraph;
	private AutomatonDrawer<S> myDrawer;
	private AffineTransform transform;

	public AutomatonEditorPanel(T m, UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		myAutomaton = m;
		myGraph = new TransitionGraph<S>(m);
		myGraph.addListener(this);
		StateDrawer vDraw = new StateDrawer();
		SelectedStateDrawer sVDraw = new SelectedStateDrawer();
		myDrawer = new AutomatonDrawer<S>(vDraw, sVDraw);
		transform = new AffineTransform();
	}

	public void stopAllEditing() {

	}

	public void editNote(Note n) {

	}

	public void setTool(Tool t) {
		this.removeMouseListener(myTool);
		this.removeMouseMotionListener(myTool);
		myTool = t;
		this.addMouseListener(myTool);
		this.addMouseMotionListener(myTool);
		Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);

		if (myTool instanceof DeleteTool) {
			Toolkit toolkit = Toolkit.getDefaultToolkit();

			String del = JFLAPConstants.RESOURCE_ROOT
					+ "/ICON/deletecursor.gif";
			Image image = toolkit.getImage(del);
			Point hotSpot = new Point(5, 5);
			cursor = toolkit.createCustomCursor(image, hotSpot, "Delete");
		}
		setCursor(cursor);
	}

	public void selectObject(Object o) {
		myGraph.setSelected(o, true);
		repaint();
	}

	public void clearSelection() {
		myGraph.clearSelection();
		repaint();
	}

	public void showMenu(Point2D p) {
		Object o = objectAtPoint(p);

	}

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

	private Note noteAtPoint(Point2D p) {
		// TODO Auto-generated method stub
		return null;
	}

	private State[] arrowAtPoint(Point2D p) {
		for (S trans : myAutomaton.getTransitions()) {
			State from = trans.getFromState(), to = trans.getToState();

			CurvedArrow arrow = myDrawer.getArrow(from, to, myGraph);
			if (CurvedArrow.intersects(p, 2, arrow))
				return new State[] { from, to };
		}
		return null;
	}

	private S transitionAtPoint(Point2D p) {
		// I think this is actually supposed to be the specific label, not the
		// arrow.
		for (S trans : myAutomaton.getTransitions()) {
			LabelBounds bounds = getLabelBounds(trans, getGraphics());
			if (bounds.contains(p))
				return trans;
		}
		return null;
	}

	private State stateAtPoint(Point2D p) {
		for (Point2D vertex : myGraph.points()) {
			if (p.distance(vertex) <= getStateRadius())
				return myGraph.vertexForPoint(vertex);
		}
		return null;
	}

	private LabelBounds getLabelBounds(S trans, Graphics g) {
		Point2D pFrom = myGraph.pointForVertex(trans.getFromState());
		Point2D pTo = myGraph.pointForVertex(trans.getToState());
		Point2D center = myGraph.getLabelCenter(trans);

		double angle = pFrom.equals(pTo) ? 0 : GeometryHelper.calculateAngle(
				pFrom, pTo);
		// calculate bounds
		FontMetrics metrics = g.getFontMetrics();
		String label = trans.getLabelText();
		int w = metrics.stringWidth(label);
		int h = metrics.getMaxAscent();
		int x = (int) (center.getX() - w / 2);
		int y = (int) (center.getY() - h / 2);

		return new LabelBounds(-angle, new Rectangle(x, y, w, h));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		setBackground(java.awt.Color.white);
		Graphics2D g2 = (Graphics2D) g;

		g2.transform(transform);
		myDrawer.draw(myGraph, g);
		// Draw all notes?

		if (myTool != null)
			myTool.draw(g);
		updateBounds(g);
	}

	private void updateBounds(Graphics g) {
		moveOutOfBoundStates(g);
		double maxx = 0, maxy = 0;
		Point2D[] points = myGraph.points();

		if (points.length > 0) {
			maxx = points[0].getX();
			maxy = points[0].getY();
			for (int i = 0; i < points.length; i++) {
				maxx = Math.max(maxx, points[i].getX());
				maxy = Math.max(maxy, points[i].getY());
			}
		}

		for (S trans : myAutomaton.getTransitions()) {
			LabelBounds bounds = getLabelBounds(trans, g);
			maxx = Math.max(maxx, bounds.getMaxX());
			maxy = Math.max(maxy, bounds.getMaxY());
		}

		if (maxx > 0 && maxy > 0) {
			maxx += getStateRadius() + 5;
			maxy += getStateRadius() + 5;

			int x = (int) Math.ceil(maxx), y = (int) Math.ceil(maxy);
			setPreferredSize(new Dimension(x, y));
		}
	}

	private void moveOutOfBoundStates(Graphics g) {
		for (S trans : myAutomaton.getTransitions()) {
			LabelBounds label = getLabelBounds(trans, g);
			double minx = label.getMinX(), miny = label.getMinY();

			State from = trans.getFromState(), to = trans.getToState();
			ControlPoint ctrl = myGraph.getControlPt(from, to);
			CurvedArrow arrow = myDrawer.getArrow(from, to, myGraph);
			Rectangle2D aBounds = arrow.getBounds();

			if (minx < 0 || miny < 0 || aBounds.getMinX() < 0
					|| aBounds.getMinY() < 0) {

				Point2D current = myGraph.pointForVertex(from);
				Point2D bounds = checkMoveBounds(from);
				double x = minx < 0 ? bounds.getX() : current.getX();
				double y = miny < 0 ? bounds.getY() : current.getY();

				// move the state, keep the ctrl point the same.
				moveState(from, new Point2D.Double(x, y));
				moveCtrlPoint(from, to, ctrl);
			}
		}
	}

	@Override
	public void setMagnification(double mag) {
		// TODO: Resize automata & notes, may actually want separate slider for
		// automaton. Issue with changing magnification and points.
		// transform.setToScale(mag * 2, mag * 2);
		super.setMagnification(mag);
		repaint();
	}

	public State createState(Point point) {
		StateSet states = myAutomaton.getStates();
		State vertex = states.createAndAddState();

		//TODO: replace AddEvent with an event that will remember the vertex's position
		getKeeper().registerChange(new AddEvent<State>(states, vertex));
		return vertex;
	}

	public S createTransition(State from, State to) {
		StateSet states = myAutomaton.getStates();

		if (!(states.contains(from) && states.contains(to)))
			return null;
		return myAutomaton.createBlankTransition(from, to);
	}

	public void editTransition(S trans, boolean isNew) {
		TransitionTable table = TransitionTableFactory.createTable(trans,
				myAutomaton, this);
		table.setCellSelectionEnabled(true);
		table.changeSelection(0, 0, false, false);
		table.requestFocus();
		revalidate();
		repaint();

		final Dimension tableSize = table.getSize();
		Point2D center = isNew ? calculateCenterPoint(trans) : myGraph
				.getLabelCenter(trans);

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

		repaint();
	}

	public void moveCtrlPoint(State from, State to, Point2D point) {
		ControlPoint ctrl = myGraph.getControlPt(from, to);
		ctrl.setLocation(point);
		for (S trans : myGraph.getOrderedTransitions(from, to))
			myGraph.setControlPt(ctrl, trans);
	}

	public void moveState(State s, Point2D p) {
		Point2D bounds = checkMoveBounds(s);

		if (p.getX() >= bounds.getX() && p.getY() >= bounds.getY())
			myGraph.moveVertex(s, p);
	}

	private Point2D checkMoveBounds(State s) {
		double xBounds = getStateRadius() + 5;
		double yBounds = xBounds;
		Point2D statePoint = myGraph.pointForVertex(s);
		State start = myAutomaton.getStartState();

		if (start != null && start.equals(s))
			xBounds += getStateRadius();

		for (S trans : myAutomaton.getTransitions()) {
			State from = trans.getFromState(), to = trans.getToState();

			if (from.equals(s) || to.equals(s)) {
				ControlPoint ctrl = myGraph.getControlPt(from, to);
				LabelBounds label = getLabelBounds(trans, getGraphics());
				Rectangle lBounds = label.getBounds();

				// Update bounds if labels would be off screen
				if (lBounds.getMinX() <= 5)
					xBounds = Math.max(xBounds, lBounds.getWidth() / 2);
				if (lBounds.getMinY() <= 5)
					yBounds = Math.max(yBounds,
							statePoint.getY() - lBounds.getMinY());
			}
		}
		return new Point2D.Double(xBounds, yBounds);
	}

	private double getStateRadius() {
		return myDrawer.getVertexDrawer().getVertexRadius();
	}

	@Override
	public void toolActivated(Tool e) {
		setTool(e);
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		revalidate();
		repaint();
	}

	public Point2D getPointForVertex(State vertex) {
		return myGraph.pointForVertex(vertex);
	}

	public Point2D calculateCenterPoint(S trans) {
		State from = trans.getFromState(), to = trans.getToState();

		if (myGraph.hasEdge(from, to)) {
			List<S> order = myGraph.getOrderedTransitions(from, to);
			return myGraph.getCenterPoint(trans, order.size(), from, to);
		}

		Point2D pFrom = myGraph.pointForVertex(from), pTo = myGraph
				.pointForVertex(to);
		Point2D ctrl = myGraph.getDefaultControlPoint(from, to);
		return GeometryHelper.getCurveCenter(pFrom, ctrl, pTo);

	}

	public void removeState(State vertex) {
		StateSet s = myAutomaton.getStates();
		s.remove(vertex);
		//TODO: replace RemoveEvent with an event that will remember the vertex's position
		getKeeper().registerChange(new RemoveEvent<State>(s, vertex));
	}

	public void removeTransition(S trans) {
		TransitionSet<S> transitions = myAutomaton.getTransitions();
		transitions.remove(trans);
		getKeeper().registerChange(new RemoveEvent<S>(transitions, trans));
	}

	public void removeEdge(State from, State to) {
		S[] temp = (S[]) myGraph.getOrderedTransitions(from, to).toArray(
				new Transition[0]);

		for (S trans : temp)
			removeTransition(trans);
		//TODO: create Compound Remove Action
	}
}
