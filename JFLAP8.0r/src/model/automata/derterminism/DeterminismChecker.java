package model.automata.derterminism;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.formaldef.components.symbols.SymbolString;

public abstract class DeterminismChecker<T extends Transition> {

	
	public DeterminismChecker(){
	}

	
	public boolean isDeterministic(Automaton<T> m){
		return this.getNondeterministicStates(m).length == 0;
	}


	public State[] getNondeterministicStates(Automaton<T> m) {
		
		Collection<State> states = new ArrayList<State>();
		for (State s: m.getStates()){
			if (isNondeterministic(s, m)){
				states .add(s);
			}
		}
		
		return states.toArray(new State[0]);
	}


	private boolean isNondeterministic(State s, Automaton<T> m) {
		return !getNondeterministicTransitionsForState(s,m).isEmpty();
	}

	public Collection<T> getAllNondeterministicTransitions(Automaton<T> m){
		Collection<T> trans = new ArrayList<T>();
		for (State s: m.getStates()){
			trans.addAll(getNondeterministicTransitionsForState(s, m));
		}
		
		return trans;
	}
	
	private Collection<T> getNondeterministicTransitionsForState(State s,
			Automaton<T> m) {
		
		Map<SymbolString, ArrayList<T>> dMap = new HashMap<SymbolString, ArrayList<T>>();
		
		Set<T> from = m.getTransitions().getTransitionsFromState(s);
		
		for (T trans: from){
			SymbolString compare = retrieveApplicableString(trans);
			ArrayList<T> list = dMap.get(compare);
			if (list == null)
				list = new ArrayList<T>();
			list.add(trans);
			dMap.put(compare, list);
		}
		
		ArrayList<T> list = new ArrayList<T>();
		for(ArrayList<T> t: dMap.values()){
			if (t.size() > 1)
				list.addAll(t);
				
		}
		
		return list;
	}


	protected abstract SymbolString retrieveApplicableString(T trans);
}
