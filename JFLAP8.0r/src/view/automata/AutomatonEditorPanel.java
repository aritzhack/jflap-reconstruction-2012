package view.automata;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.automata.State;
import model.automata.StateSet;
import model.automata.Transition;
import model.change.events.AddEvent;
import model.graph.ControlPoint;
import model.graph.TransitionGraph;
import model.undo.UndoKeeper;
import util.JFLAPConstants;
import util.arrows.GeometryHelper;
import view.EditingPanel;
import view.automata.tools.Tool;
import view.automata.tools.ToolListener;

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
		return null;
	}

	private S transitionAtPoint(Point2D p) {
		for (S trans : myAutomaton.getTransitions()) {
			State from = trans.getFromState(), to = trans.getToState();
			ControlPoint ctrl = myGraph.getControlPt(from, to);

			if (p.distance(ctrl) <= JFLAPConstants.CONTROL_POINT_RADIUS)
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

		return new LabelBounds(angle, new Rectangle(x, y, w, h));
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
		// I believe all transformations have to do with the slider (ie. setting
		// a different scale)
		// Graphics2D g2 = (Graphics2D) g;
		// double newXScale = 1.0/transform.getScaleX();
		// double newYScale = 1.0/transform.getScaleY();
		// g2.scale(newXScale, newYScale);
		// g2.translate(-transform.getTranslateX(), -transform.getTranslateY());
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

			if (minx < 0 || miny < 0 || ctrl.getX() < 0 || ctrl.getY() < 0) {

				Point2D current = myGraph.pointForVertex(from);
				Point2D bounds = checkMoveBounds(from);
				double x = minx < 0 ? bounds.getX() : current.getX();
				double y = miny < 0 ? bounds.getY() : current.getY();

				// move the state, keep the ctrl point the same.
				moveState(from, new Point2D.Double(x, y));
				moveCtrlPoint(trans, ctrl);
			}
		}
	}

	@Override
	public void setMagnification(double mag) {
		// TODO: Resize automata & notes, may actually want separate slider for
		// automaton. Issue with changing magnification and points.
		transform.setToScale(mag * 2, mag * 2);
		super.setMagnification(mag);
		repaint();
	}

	public State createState(Point point) {
		StateSet states = myAutomaton.getStates();
		State vertex = states.createAndAddState();

		myGraph.addVertex(vertex, point);
		getKeeper().applyAndListen(new AddEvent<State>(states, vertex));
		return vertex;
	}
	
	public void createTransition(State from, State to){
		myGraph.addEdge(from, to);
		//TODO: UndoKeeping
	}

	public void moveCtrlPoint(S trans, Point2D point) {
		State from = trans.getFromState(), to = trans.getToState();
		ControlPoint ctrl = myGraph.getControlPt(from, to);
		ctrl.setLocation(point);
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

		if (myAutomaton.getStartState().equals(s))
			xBounds += getStateRadius();

		for (S trans : myAutomaton.getTransitions())
			if (trans.getFromState().equals(s) || trans.getToState().equals(s)) {
				ControlPoint ctrl = myGraph.getControlPt(trans.getFromState(),
						trans.getToState());
				LabelBounds label = getLabelBounds(trans, getGraphics());
				Rectangle r = label.getBounds();

				// Update bounds if ctrl point or labels would be are off screen
				if (ctrl.getX() <= JFLAPConstants.CONTROL_POINT_RADIUS + 5)
					xBounds = Math
							.max(xBounds,
									statePoint.getX()
											- (ctrl.getX() - JFLAPConstants.CONTROL_POINT_RADIUS)
											+ 5);
				if (ctrl.getY() <= JFLAPConstants.CONTROL_POINT_RADIUS + 5)
					yBounds = Math
							.max(yBounds,
									statePoint.getY()
											- (ctrl.getY() - JFLAPConstants.CONTROL_POINT_RADIUS)
											+ 5);
				if (r.getMinX() <= 5)
					xBounds = Math.max(xBounds, r.getWidth() / 2);
				if (r.getMinY() <= 5)
					yBounds = Math
							.max(yBounds, statePoint.getY() - r.getMinY());
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
	
	public TransitionGraph<S> getGraph() {
		return myGraph;
	}
}
