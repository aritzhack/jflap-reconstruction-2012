package model.automata.closure;

import java.util.TreeSet;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.TransitionSet;

public class ClosureHelper {

	public static TreeSet<State> takeClosure(State s, Automaton m){
		TreeSet<State> closure = new TreeSet<State>();

		TransitionSet<? extends Transition> set = m.getTransitions();

		closure.add(s);
		boolean changed = true;
		
		while (changed){
			changed = false;
			for (State state: new TreeSet<State>(closure)){
				for (Transition t: set.getTransitionsFromState(state)){
					if (t.isLambdaTransition()){
						changed = closure.add(t.getToState()) || changed;
					}
				}
			}
		}
		return closure;
	}

}
