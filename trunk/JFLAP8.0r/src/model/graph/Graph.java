package model.graph;

import java.util.Set;
import java.util.TreeSet;

public class Graph {
	
	private Set<Edge> myEdges;
	private TreeSet<Vertex> myVertices;

	public Graph(){
		myEdges = new TreeSet<Edge>();
		myVertices = new TreeSet<Vertex>();
	}
	
	public void clear(){
		myEdges.clear();
		myVertices.clear();
	}

	public boolean addEdge(Edge e){
		return myEdges.add(e);
	}
	
	public boolean removeEdge(Edge e){
		return myEdges.remove(e);
	}
	
	public boolean addVertex(Vertex v){
		return myVertices.add(v);
	}
	
	public boolean removeVertex(Vertex v){
		if(myVertices.remove(v)){
			for (Edge e: myEdges.toArray(new Edge[0])){
				if (e.getFromVertex().equals(v) ||
						e.getToVertex().equals(v))
					myEdges.remove(e);
			}
			return true;
		}
		return false;
	}

	public Edge[] getEdgesFromVertex(Vertex from) {
		Set<Edge> fromSet = new TreeSet<Edge>();
		for (Edge e: myEdges.toArray(new Edge[0])){
			if (e.getFromVertex().equals(from))
				fromSet.add(e);
		}
		return fromSet.toArray(new Edge[0]);
	}
	
	public Edge[] getEdgesToVertex(Vertex to) {
		Set<Edge> toSet = new TreeSet<Edge>();
		for (Edge e: myEdges.toArray(new Edge[0])){
			if (e.getToVertex().equals(to))
				toSet.add(e);
		}
		return toSet.toArray(new Edge[0]);
	}

	public int getNumberEdges() {
		return myEdges.size();
	}

	public Vertex[] getVertices() {
		return myVertices.toArray(new Vertex[0]);
	}
	
	@Override
	public String toString() {
		return "Graph:\n" + 
					"\tEdges: " + myEdges.toString() + "\n" +
					"\tVertices: " + myVertices.toString() + "\n";
	}
}
