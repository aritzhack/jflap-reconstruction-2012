package view.automata;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Set;
import java.util.TreeSet;

import model.automata.State;
import model.automata.Transition;
import model.graph.Graph;
import model.graph.TransitionGraph;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;

/** Automaton Drawer that allows for and keeps track of selection of States, Arrows, and Transitions and draws them accordingly.
 * 
 * @author Ian McMahon
 */
public class SelectionAutomatonDrawer<T extends Transition<T>> extends
		AutomatonDrawer<T> {

	private Set<State> mySelectedStates;
	private Set<State[]> mySelectedEdges;
	private Set<T> mySelectedTrans;

	public SelectionAutomatonDrawer(StateDrawer vDraw) {
		super(vDraw);
		mySelectedStates = new TreeSet<State>();
		mySelectedEdges = new TreeSet<State[]>();
		mySelectedTrans = new TreeSet<T>();
	}

	@Override
	public void drawVertex(State v, Graph<State> obj, Graphics g) {
		TransitionGraph<T> graph = (TransitionGraph<T>) obj;
		if (isSelected(v))
			drawSelectedVertex(v, obj, g);
		else
			super.drawVertex(v, obj, g);
	}
	
	@Override
	public void drawLabel(Graphics2D g2d, T t, Point2D center) {
		if(isSelected(t))
			return;
		super.drawLabel(g2d, t, center);
	}

	/** Selects or deselects the given object based on <CODE>select</CODE> */
	public boolean setSelected(Object o, boolean select) {
		if (o instanceof State)
			return select ? mySelectedStates.add((State) o) : mySelectedStates
					.remove((State) o);
		else if (o instanceof State[])
			return select ? mySelectedEdges.add((State[]) o) : mySelectedEdges
					.remove((State[]) o);
		else if (o instanceof Transition)
			return select ? mySelectedTrans.add((T) o) : mySelectedTrans
					.remove((T) o);
		return false;
	}

	/** Returns true if the given Object is selected. */
	public boolean isSelected(Object o) {
		if (o instanceof State)
			return mySelectedStates.contains((State) o);
		else if (o instanceof State[])
			return mySelectedEdges.contains((State[]) o);
		else if (o instanceof Transition)
			return mySelectedTrans.contains((T) o);
		return false;
	}

	/** Deselects all objects. */
	public void clearSelection() {
		mySelectedStates.clear();
		mySelectedEdges.clear();
		mySelectedTrans.clear();
	}

	/** Draws the vertex as the selected state color, as set in Preferences. */
	private void drawSelectedVertex(State v, Graph<State> obj, Graphics g) {
		StateDrawer vDraw = (StateDrawer) getVertexDrawer();
		vDraw.setInnerColor(JFLAPPreferences.getSelectedStateColor());
		super.drawVertex(v, obj, g);
		vDraw.setInnerColor(JFLAPPreferences.getStateColor());
	}

	/** Draws the control point p. */
	private void drawPoint(Graphics g, Point2D p) {
		g.drawOval((int) p.getX() - JFLAPConstants.CONTROL_POINT_RADIUS,
				(int) p.getY() - JFLAPConstants.CONTROL_POINT_RADIUS,
				JFLAPConstants.CONTROL_POINT_RADIUS * 2,
				JFLAPConstants.CONTROL_POINT_RADIUS * 2);
	}
}
