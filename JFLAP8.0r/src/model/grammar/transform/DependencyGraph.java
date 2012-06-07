package model.grammar.transform;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Variable;
import model.grammar.VariableAlphabet;
import model.graph.DirectedGraph;
import model.graph.Graph;
import model.graph.PathFinder;
import model.graph.layout.CircleLayoutAlgorithm;

public class DependencyGraph extends DirectedGraph<Variable>{

	
	public DependencyGraph(VariableAlphabet vars){
		for (Symbol v: vars){
			addVertex((Variable) v, new Point2D.Double());
		}
		CircleLayoutAlgorithm alg = new CircleLayoutAlgorithm();
		alg.layout(this, new HashSet<Variable>());
	}
	
	public boolean addDependency(Variable from, Variable to){
		if (from.equals(to))
			return false;
		
		return super.addEdge(from, to);

	}
	
	public boolean removeDependence(Variable from, Variable to){
		return removeEdge(from, to);
	}

	public int getNumberDependencies() {
		return super.totalDegree();
	}
	
	public Variable[] getAllDependencies(Variable var){
		return adjacent(var).toArray(new Variable[0]);
	}
	
}
