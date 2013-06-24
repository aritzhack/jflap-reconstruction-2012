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

public class AutomatonDrawer<T extends Transition<T>> extends GraphDrawer<State> {

	private SelectedStateDrawer mySelectedDrawer;


	public AutomatonDrawer(StateDrawer vDraw, SelectedStateDrawer sVDraw) {
		super(vDraw);
		mySelectedDrawer = sVDraw;
	}
	
	@Override
	public void drawVertex(State v, Graph<State> obj, Graphics g) {
		TransitionGraph<T> graph = (TransitionGraph<T>) obj;
		if(graph.isSelected(v))
			mySelectedDrawer.draw(obj.pointForVertex(v), v, g);
		else super.drawVertex(v, obj, g);
	}

	@Override
	public void drawLabel(State from, State to, Graph<State> obj, Graphics g) {
		TransitionGraph<T> graph = (TransitionGraph<T>) obj;
		List<T> transitions = graph.getOrderedTransitions(from, to);
		Point2D pFrom = graph.pointForVertex(from);
		Point2D pTo = graph.pointForVertex(to);
//		Point2D ctrl = graph.getControlPt(from, to);
		
		drawLabels(transitions, g, graph, pFrom, pTo);
//		drawPoint(g, ctrl);
	}

	private void drawLabels(List<T> transitions, Graphics g,
			TransitionGraph<T> graph, Point2D pFrom, Point2D pTo) {
		//draw Labels
		Graphics2D g2d = (Graphics2D) g.create();
		AffineTransform oldTX = g2d.getTransform();
		for(int i=0; i<transitions.size();i++){
			T t = transitions.get(i);

			//set up transform
			Point2D center = graph.getLabelCenter(t);
			
			g2d.setTransform(oldTX);
			if (!t.isLoop()){
				AffineTransform tx = createLabelTransform(center,pFrom,pTo);
				g2d.transform(tx);
			}
			//drawLabel
			drawLabel(g2d, t, center);
		}
	}

	private void drawPoint(Graphics g, Point2D p) {
		g.drawOval((int)p.getX() - JFLAPConstants.CONTROL_POINT_RADIUS, 
				(int)p.getY() - JFLAPConstants.CONTROL_POINT_RADIUS, 
				JFLAPConstants.CONTROL_POINT_RADIUS * 2,
				JFLAPConstants.CONTROL_POINT_RADIUS * 2);	
	}

	public void drawLabel(Graphics2D g2d, T t, Point2D center) {
		String label = t.getLabelText();
		FontMetrics metrics = g2d.getFontMetrics();
		int w = metrics.stringWidth(label);
		int h = metrics.getMaxAscent();
		int x = (int) (center.getX()-w/2);
		int y = (int) (center.getY()+h/2);
		g2d.drawString(label, x, y);
	}
	
		
	private AffineTransform createLabelTransform(Point2D center, Point2D from, Point2D to) {
		AffineTransform at = new AffineTransform();
		double angle =  GeometryHelper.calculateAngle(from,to) % (2*Math.PI);

		at.rotate(angle % Math.PI, center.getX(), center.getY());
		return at;
	}


}
