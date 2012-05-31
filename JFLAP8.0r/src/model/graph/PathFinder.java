package model.graph;

import java.util.Set;
import java.util.TreeSet;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;

/**
 * A helper class that finds simple paths in any automaton
 * graph. This is a very basic implementation, and this
 * class could be made much more potent/useful!
 * 
 * @author Julian Genkins
 *
 */
public class PathFinder {

	private Graph myGraph;
	private Set<Vertex> myVisited;

	public PathFinder(Graph g) {
		myGraph = g;
		myVisited = new TreeSet<Vertex>();
	}

	public PathFinder(Automaton m) {
		this(GraphHelper.convertToGraph(m));
	}

	public Vertex[] findPath(Vertex from, Vertex to) {
		Vertex[] path = recurseForPath(from, to);
		clear();
		return path;
	}

	private void clear() {
		myVisited.clear();
	}

	private Vertex[] recurseForPath(Vertex from, Vertex to) {
		if (myVisited.contains(from))
			return null;
		
		myVisited.add(from);
		
		if (from.equals(to))
			return new Vertex[]{to};
		

		for (Edge t: myGraph.getEdgesFromVertex(from))
		{
			Vertex[] path = recurseForPath(t.getToVertex(), to);
			if (path != null)
				return path;
		}
		return null;
	}
	
	

}
