package view.graph;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

import util.JFLAPConstants;
import util.arrows.ArrowHead;
import util.arrows.CurvedArrow;
import util.arrows.GeometryHelper;

import debug.JFLAPDebug;

import model.graph.Graph;

public class GraphDrawer<T> implements JFLAPConstants {

	private VertexDrawer<T> myVertexDrawer;

	
	public GraphDrawer(VertexDrawer<T> vDraw){
		myVertexDrawer = vDraw;
	}


	public void draw(Graph<T> obj, Graphics g) {
		drawEdges(obj,g);
		drawVertices(obj, g);
	}

	public void drawVertices(Graph<T> obj, Graphics g){
		for (T v : obj.vertices()){
			drawVertex(v, obj, g);
		}
	}
	
	public void drawVertex(T v, Graph<T> obj, Graphics g) {
		myVertexDrawer.draw(obj.pointForVertex(v), v,g);
	}

	public void drawEdges(Graph<T> obj, Graphics g) {
		for (T from : obj.vertices()){
			for (T to: obj.adjacent(from)){
				drawEdge(from, to, obj, g);
				drawLabel(from,to,obj,g);
			}
		}
	}

	public void drawLabel(T from, T to, Graph<T> obj, Graphics g) {
		
	}

	public void drawEdge(T from, T to, Graph<T> obj, Graphics g) {
		CurvedArrow curve = getArrow(from, to, obj);
		curve.draw(g);
	}
	
	public CurvedArrow getArrow(T from, T to, Graph<T> obj) {
		Point2D pFrom = obj.pointForVertex(from);
		Point2D pTo = obj.pointForVertex(to);
		Point2D ctrl = obj.getControlPt(from,to);
		double rad = getVertexDrawer().getVertexRadius();
		double theta1 = GeometryHelper.calculateAngle(pFrom, pTo),
				theta2=GeometryHelper.calculateAngle(pTo, pFrom);
		if (from.equals(to)){
			theta1=-3*Math.PI/4;
			theta2=-Math.PI/4;
		}
			
		Point2D edgeFrom = GeometryHelper.pointOnCircle(pFrom,rad,theta1);
		Point2D edgeTo = GeometryHelper.pointOnCircle(pTo,rad,theta2);
		
		double arrowheadLen = 0;
		if (obj.isDirected()) arrowheadLen=ARROW_LENGTH;
		CurvedArrow curve = new CurvedArrow(arrowheadLen, ARROW_ANGLE);
		curve.setCurve(edgeFrom, ctrl, edgeTo);
		return curve;
	}

		
	public VertexDrawer<T> getVertexDrawer(){
		return myVertexDrawer;
	}
	
}
