package model.automata;

import java.util.Set;
import java.util.TreeSet;

/**
 * A helper class that finds simple paths in any automaton
 * graph. This is a very basic implementation, and this
 * class could be made much more potent/useful!
 * 
 * @author Julian Genkins
 *
 */
public class PathFinder {

	private Automaton<? extends Transition> myAutomaton;
	private Set<State> myVisited;

	public PathFinder(Automaton m) {
		myAutomaton = m;
		myVisited = new TreeSet<State>();
	}

	public State[] findPath(State from, State to) {
		State[] path = recurseForPath(from, to);
		clear();
		return path;
	}

	private void clear() {
		myVisited.clear();
	}

	private State[] recurseForPath(State from, State to) {
		if (myVisited.contains(from))
			return null;
		
		myVisited.add(from);
		
		if (from.equals(to))
			return new State[]{to};
		

		for (Transition t: myAutomaton.getTransitions().getTransitionsFromState(from))
		{
			State[] path = recurseForPath(t.getToState(), to);
			if (path != null)
				return path;
		}
		return null;
	}
	
	

}
