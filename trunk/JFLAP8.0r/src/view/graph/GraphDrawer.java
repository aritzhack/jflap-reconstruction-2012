package view.graph;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

import debug.JFLAPDebug;

import model.graph.Graph;

public class GraphDrawer<T> {

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
			for (T to: obj.adjacent(from))
				drawEdge(from, to, obj, g);
		}
	}

	public void drawEdge(T from, T to, Graph<T> obj, Graphics g) {
		Point2D pFrom = obj.pointForVertex(from);
		Point2D pTo = obj.pointForVertex(to);
		g.drawLine((int)pFrom.getX(),(int)pFrom.getY(), 
					(int)pTo.getX(),(int) pTo.getY());
		

	}
	
	public VertexDrawer<T> getVertexDrawer(){
		return myVertexDrawer;
	}
	
}
