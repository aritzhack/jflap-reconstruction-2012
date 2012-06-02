package model.grammar.transform;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Variable;
import model.grammar.VariableAlphabet;
import model.graph.Edge;
import model.graph.Graph;
import model.graph.PathFinder;
import model.graph.Vertex;

public class DependencyGraph {

	private Graph myGraph;
	private Map<Variable, Vertex> myVarToVertexMap;
	
	public DependencyGraph(VariableAlphabet vars){
		myGraph = new Graph();
		myVarToVertexMap = new TreeMap<Variable, Vertex>();
		for (Symbol v: vars){
			createAndAddVertexForVar((Variable) v);
		}
		//TODO: apply circle layout
	}
	
	private void createAndAddVertexForVar(Variable v) {
		Vertex toAdd = new Vertex(v.toString());
		
		if (myGraph.addVertex(toAdd));
			myVarToVertexMap.put(v, toAdd);
	}

	public boolean addDependency(Variable from, Variable to){
		if (from.equals(to))
			return false;
		
		return myGraph.addEdge(createEdge(from, to));

	}
	
	private Edge createEdge(Variable from, Variable to) {
		Vertex f = getVertexForVariable(from);
		Vertex t = getVertexForVariable(to);
		
		if (f == null || t == null)
			throw new RuntimeException();
		return new Edge(f,t);
	}

	public boolean removeDependence(Variable from, Variable to){
		return myGraph.removeEdge(createEdge(from, to));
	}

	private Vertex getVertexForVariable(Variable from) {
		return myVarToVertexMap.get(from);
	}

	public int getNumberDependencies() {
		return myGraph.getNumberEdges();
	}
	
	public Variable[] getAllDependencies(Variable var){
		Vertex from = this.getVertexForVariable(var);
		PathFinder finder = new PathFinder(myGraph);
		Set<Variable> dep = new TreeSet<Variable>();
		for (Vertex to: myGraph.getVertices()){
			if (finder.findPath(from, to) != null &&
					!from.equals(to))
				dep.add(getVariableForVertex(to));
		}
		return dep.toArray(new Variable[0]);
	}

	private Variable getVariableForVertex(Vertex to) {
		for (Entry<Variable,Vertex> e : myVarToVertexMap.entrySet()) {
			if(e.getValue().equals(to))
				return e.getKey();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return myGraph.toString();
	}
	
}
