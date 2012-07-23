package view.automata;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
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
import util.arrows.GeometryHelper;
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
		Point2D ctrl = graph.getControlPt(from, to);
		Point2D pFrom = graph.pointForVertex(from);
		Point2D pTo = graph.pointForVertex(to);
		
		drawArrow(g, ctrl, pFrom, pTo);
		
		//draw Labels
		List<T> transitions = graph.getOrderedTransitions(from, to);
		Graphics2D g2d = (Graphics2D) g.create();
		AffineTransform oldTX = g2d.getTransform();
		for(int i=0; i<transitions.size();i++){
			T t = transitions.get(i);
			//set up transform
			Point2D center = graph.getLabelCenter(t);
			AffineTransform tx = createLabelTransform(center,pFrom,pTo);
			g2d.setTransform(oldTX);
			g2d.transform(tx);
			
			//drawLabel
			drawLabel(g2d, t, center);
		}
	}

	private void drawArrow(Graphics g, Point2D ctrl, Point2D pFrom, Point2D pTo) {
		double rad = getVertexDrawer().getVertexRadius();
		Point2D edgeFrom = GeometryHelper.pointOnCircle(pFrom,rad,ctrl);
		Point2D edgeTo = GeometryHelper.pointOnCircle(pTo,rad,ctrl);
		CurvedArrow arrow = new CurvedArrow(edgeFrom, ctrl, edgeTo, ARROW_LENGTH, ARROW_ANGLE);
		arrow.draw(g);
	}

	public void drawLabel(Graphics2D g2d, T t, Point2D center) {
		String label = t.getLabelText();
		FontMetrics metrics = g2d.getFontMetrics();
		int w = metrics.stringWidth(label);
		int h = metrics.getMaxAscent();
		int x = (int) (center.getX()-w/2);
		int y = (int) (center.getY()-h/2);
		g2d.drawString(label, x, y);
		g2d.drawRect(x-1, y-h+1, w, h);
	}
	
		
	private AffineTransform createLabelTransform(Point2D center, Point2D from, Point2D to) {
		AffineTransform at = new AffineTransform();
		double angle =  GeometryHelper.calculateAngle(from,to) % (2*Math.PI);

		at.rotate(angle % Math.PI, center.getX(), center.getY());
		if (angle > Math.PI/2 && angle <= Math.PI*1.5){
			at.translate(0, 2*JFLAPConstants.EDITOR_CELL_HEIGHT);
		}
		return at;
	}


}
