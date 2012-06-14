package model.automata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import debug.JFLAPDebug;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.symbols.Symbol;

public class TransitionSet<T extends Transition> extends FunctionSet<T> {

	private TreeMap<State, Set<T>> transitionsFromStateMap;


	private TreeMap<State, Set<T>> transitionsToStateMap;


	public TransitionSet(){
		transitionsFromStateMap = new TreeMap<State, Set<T>>();
		transitionsToStateMap = new TreeMap<State, Set<T>>();
	}

	@Override
	public Character getCharacterAbbr() {
		return "\u03B4".charAt(0);
	}

	@Override
	public String getDescriptionName() {
		return "Transitions";
	}

	@Override
	public String getDescription() {
		return "The set of transition functions which" +
				" define the language.";
	}

	public Set<T> getTransitionsFromState(State from) {
		Set<T> desired = transitionsFromStateMap.get(from);
		if (desired == null)
			return new TreeSet<T>();		
		return new TreeSet<T>(desired);
	}

	public Set<T> getTransitionsToState(State to) {
		Set<T> desired = transitionsToStateMap.get(to);
		if (desired == null)
			return new TreeSet<T>();		
		return new TreeSet<T>(desired);	}

	public Set<T> getTransitionsFromStateToState(State from, State to) {
		Set<T> toreturn = getTransitionsFromState(from);
		toreturn.retainAll(getTransitionsToState(to));
		return toreturn;
	}

	@Override
	public boolean add(T trans){
		if (!super.add(trans)) return false;
		
		
		Set<T> fromList = transitionsFromStateMap.get(trans.getFromState());
		if (fromList == null){
			fromList = new TreeSet<T>();
		}
		fromList.add(trans);
		transitionsFromStateMap.put(trans.getFromState(), fromList);
		if (transitionsFromStateMap.get(trans.getToState()) == null){
			transitionsFromStateMap.put(trans.getToState(), new TreeSet<T>());
		}
		
		Set<T> toList = transitionsToStateMap.get(trans.getToState());
		if (toList == null){
			toList = new TreeSet<T>();
		}
		toList.add(trans);
		transitionsToStateMap.put(trans.getToState(), toList);
		if (transitionsToStateMap.get(trans.getFromState()) == null){
			transitionsToStateMap.put(trans.getFromState(), new TreeSet<T>());
		}
		
		return true;
	}

	@Override
	public boolean remove(Object trans){
		if (!super.remove(trans)) return false;
		T t = (T) trans;
		//update fromStateMap
		Set<T> fromFromSet = transitionsFromStateMap.get(t.getFromState());
		Set<T> toToSet = transitionsToStateMap.get(t.getToState());
		
		fromFromSet.remove(t);
		toToSet.remove(t);
		
//		JFLAPDebug.print(t.getToState(),6);
//		JFLAPDebug.print(transitionsFromStateMap,1);

		if (transitionsFromStateMap.get(t.getToState()).isEmpty() &&
				toToSet.isEmpty()){
			transitionsFromStateMap.remove(t.getToState());
			transitionsToStateMap.remove(t.getToState());
		}

		if (getTransitionsToState(t.getFromState()).isEmpty() &&
				fromFromSet.isEmpty()){
			transitionsFromStateMap.remove(t.getFromState());
			transitionsToStateMap.remove(t.getFromState());
		}
		
		
		return true;
	}

	public void purgeofInputSymbol(Symbol s){
		for (Transition t: this){
			t.getInput().purgeOfSymbol(null, s);
		}
	}
	
	@Override
	public void clear() {
		super.clear();
		transitionsFromStateMap.clear();
		transitionsToStateMap.clear();
	}

	@Override
	public TransitionSet<T> copy() {
		return (TransitionSet<T>) super.copy();
	}

	/**
	 * Removes all transitions with a from or to state corresponding
	 * to the parameter.
	 * 
	 * @param s
	 */
	public void removeForState(State s) {
		this.removeAll(this.getTransitionsFromState(s));
		this.removeAll(this.getTransitionsToState(s));
	}

}
