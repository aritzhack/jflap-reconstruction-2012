package util.view;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.mealy.MealyOutputFunction;
import model.graph.Graph;
import model.graph.TransitionGraph;
import model.symbols.SymbolString;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.arrows.GeometryHelper;
import view.automata.LabelBounds;

public class GraphHelper {

	/**
	 * Reforms the points so they are enclosed within a certain frame.
	 * 
	 * @param <T>
	 */
	public static <T> void moveWithinFrame(Graph<T> g, Rectangle2D bounds) {
		Object[] vertices = g.vertices().toArray();
		if (vertices.length == 0)
			return;
		Point2D p = g.pointForVertex((T) vertices[0]);
		double minx = p.getX(), miny = p.getY(), maxx = minx, maxy = miny;
		for (int i = 1; i < vertices.length; i++) {
			p = g.pointForVertex((T) vertices[i]);
			minx = Math.min(minx, p.getX());
			miny = Math.min(miny, p.getY());
			maxx = Math.max(maxx, p.getX());
			maxy = Math.max(maxy, p.getY());
		}

		minx -= JFLAPConstants.STATE_RADIUS + 5;
		miny -= JFLAPConstants.STATE_RADIUS + 5;
		maxx += JFLAPConstants.STATE_RADIUS + 5;
		maxy += JFLAPConstants.STATE_RADIUS + 5;
		// Now, scale them!
		for (int i = 0; i < vertices.length; i++) {
			p = g.pointForVertex((T) vertices[i]);
			p = new Point2D.Double((p.getX() - minx) * bounds.getWidth()
					/ (maxx - minx) + bounds.getX(), (p.getY() - miny)
					* bounds.getHeight() / (maxy - miny) + bounds.getY());
			g.moveVertex((T) vertices[i], p);
		}
	}

	public static <S extends Transition<S>> Point2D calculateCenterPoint(
			TransitionGraph<S> graph, S trans) {
		State from = trans.getFromState(), to = trans.getToState();

		if (graph.hasEdge(from, to)) {
			List<S> order = graph.getOrderedTransitions(from, to);
			return graph.getLabelCenterPoint(trans, order.size(), from, to);
		}

		Point2D pFrom = graph.pointForVertex(from), pTo = graph
				.pointForVertex(to);
		Point2D ctrl = graph.getDefaultControlPoint(from, to);
		return GeometryHelper.getCurveCenter(pFrom, ctrl, pTo);
	}

	/**
	 * Returns the bounding rectangle for the text component of the specified
	 * transition based off the font of the graphics.
	 */
	public static <S extends Transition<S>> LabelBounds getLabelBounds(
			TransitionGraph<S> graph, S trans, Graphics g) {
		Point2D pFrom = graph.pointForVertex(trans.getFromState());
		Point2D pTo = graph.pointForVertex(trans.getToState());
		Point2D center = graph.getLabelCenter(trans);

		double angle = pFrom.equals(pTo) ? 0 : GeometryHelper.calculateAngle(
				pFrom, pTo);
		// calculate bounds
		FontMetrics metrics = g.getFontMetrics();
		String label = getLabelText(graph, trans);
		int w = metrics.stringWidth(label);
		int h = metrics.getMaxAscent();
		int x = (int) (center.getX() - w / 2);
		int y = (int) (center.getY() - h / 2);

		return new LabelBounds(angle, new Rectangle(x, y, w, h));
	}
	
	public static <T extends Transition<T>> String getLabelText(TransitionGraph<T> obj, T t) {
		Automaton<T> auto = obj.getAutomaton();
		String label = t.getLabelText();
		if(auto instanceof MealyMachine){
			OutputFunctionSet<MealyOutputFunction> funcs = ((MealyMachine) auto).getOutputFunctionSet();
			SymbolString out = funcs.getOutputForTransition((FSATransition) t);
			label += " ; "+ (out == null ? JFLAPPreferences.getEmptyString() : out.toString());
		}
		return label;
	}

	/**
	 * Returns a point that is located at the max X and max Y value of either
	 * the given point or a State boundary.
	 */
	public static Point2D getOnscreenPoint(boolean isStartState, Point2D p) {
		double xBounds = JFLAPConstants.STATE_RADIUS + 5;
		double yBounds = xBounds;

		if (isStartState)
			xBounds += JFLAPConstants.STATE_RADIUS;

		double x = Math.max(p.getX(), xBounds);
		double y = Math.max(p.getY(), yBounds);
		return new Point2D.Double(x, y);
	}
}
