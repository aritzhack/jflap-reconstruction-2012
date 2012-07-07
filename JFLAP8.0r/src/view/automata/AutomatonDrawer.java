package view.automata;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Set;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.graph.Graph;
import model.graph.TransitionGraph;

import util.JFLAPConstants;
import util.Point2DAdv;
import util.arrows.CurvedArrow;
import view.graph.GraphDrawer;
import view.graph.VertexDrawer;

public class AutomatonDrawer<T extends Transition<T>> extends GraphDrawer<State> implements JFLAPConstants{

	public AutomatonDrawer(StateDrawer vDraw) {
		super(vDraw);
	}

	@Override
	public void drawVertex(State v, Graph<State> obj, Graphics g) {
		super.drawVertex(v, obj, g);
		TransitionGraph<T> graph = (TransitionGraph<T>) obj;
	}

	@Override
	public void drawEdge(State from, State to, Graph<State> obj, Graphics g) {
		TransitionGraph<T> graph = (TransitionGraph<T>) obj;
		List<T> transitions = graph.getOrderedTransitions(from, to);
		Point2D ctrl = graph.getControlPt(from, to);
		Point2D pFrom = graph.pointForVertex(from);
		Point2D pTo = graph.pointForVertex(to);
		CurvedArrow arrow = new CurvedArrow(pFrom, ctrl, pTo, ARROW_LENGTH, ARROW_ANGLE);
		arrow.draw(g);
		JFLAPDebug.print("Drawn");

	}
	

	private static Point calculateEndPoint(Transition t) {
		int shift = t.isLoop() ? 10 : 0;
		return pointOnState(
				t.getToState(),
				CurvedArrowHelper.calculateAngle(t.getFromState().getPoint(), t
						.getToState().getPoint())
						+ Math.PI - shift);
	}

	private static Point calculateStartPoint(Transition t) {
		int shift = t.isLoop() ? 10 : 0;
		return pointOnState(
				t.getFromState(),
				CurvedArrowHelper.calculateAngle(t.getFromState().getPoint(), t
						.getToState().getPoint())
						+ shift);
	}

	/**
	 * Given a state and an angle, if we treat the state as a circle, what point
	 * does that angle represent?
	 * 
	 * @param state
	 *            the state
	 * @param angle
	 *            the angle on the state
	 * @return the point on the outside of the state with this angle
	 */
	private static Point2DAdv pointOnVertex(Point2D center, double rad, double angle) {
		Point2DAdv point = new Point2DAdv(center);
		double x = Math.cos(angle) * rad;
		double y = Math.sin(angle) * rad;
		point.translate((int) x, (int) y);
		return point;
	}
	

}
