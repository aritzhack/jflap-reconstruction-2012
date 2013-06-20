package view.automata;

import java.awt.Color;
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
import model.graph.TransitionGraph;
import model.undo.UndoKeeper;
import util.JFLAPConstants;
import util.arrows.GeometryHelper;
import view.EditingPanel;
import view.automata.tools.Tool;
import view.automata.tools.ToolListener;

public class AutomatonEditorPanel<T extends Automaton<S>, S extends Transition<S>>
		extends EditingPanel implements ToolListener, ChangeListener{

	private Tool myTool;
	private T myAutomaton;
	private TransitionGraph<S> myGraph;
	private AutomatonDrawer<S> myDrawer;
	private AffineTransform transform;
	private boolean needsResizing;

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
//		for(Transition t : myAutomaton.getTransitions())
//			if()
		return null;
	}

	private State stateAtPoint(Point2D p) {
		for(Point2D vertex : myGraph.points()){
			if(p.distance(vertex) <= JFLAPConstants.STATE_RADIUS)
				return myGraph.vertexForPoint(vertex);
		}
		return null;
	}

	private LabelBounds getLabelBounds(Point2D center, Point2D pFrom,
			Point2D pTo, Graphics g) {

		double angle = GeometryHelper.calculateAngle(pFrom, pTo);
		// calculate bounds
		FontMetrics metrics = g.getFontMetrics();
		String label = this.toString();
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
		//Draw all notes?
		
		if (myTool != null)
			myTool.draw(g);
//		I believe all transformations have to do with the slider (ie. setting a different scale)
//		Graphics2D g2 = (Graphics2D) g;
//		double newXScale = 1.0/transform.getScaleX();
//		double newYScale = 1.0/transform.getScaleY();
//		g2.scale(newXScale, newYScale);
//		g2.translate(-transform.getTranslateX(), -transform.getTranslateY());
	}
	
	@Override
	public void setMagnification(double mag) {
		//TODO: Resize automata & notes, may actually want separate slider for automaton
		super.setMagnification(mag);
	}
	
	public State createState(Point point){
		StateSet states = myAutomaton.getStates();
		State vertex = states.createAndAddState();
		
		myGraph.addVertex(vertex, point);
		getKeeper().applyAndListen(new AddEvent<State>(states, vertex));
		repaint();
		return vertex;
	}
	
	public void moveState(State s, Point p){
		myGraph.moveVertex(s, p);
		repaint();
	}

	@Override
	public void toolActivated(Tool e) {
		setTool(e);
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
//		needsResizing = true;
		repaint();
	}
}
