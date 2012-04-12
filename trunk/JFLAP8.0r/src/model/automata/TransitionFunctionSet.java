package model.automata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.symbols.Symbol;

public class TransitionFunctionSet<T extends Transition> extends FunctionSet<T> {

	private HashMap<State, Set<T>> transitionsFromStateMap;


	private HashMap<State, Set<T>> transitionsToStateMap;


	public TransitionFunctionSet(){
		transitionsFromStateMap = new HashMap<State, Set<T>>();
		transitionsToStateMap = new HashMap<State, Set<T>>();
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
		return new TreeSet<T>(transitionsFromStateMap.get(from));
	}

	public Set<T> getTransitionsToState(State to) {
		return new TreeSet<T>(transitionsToStateMap.get(to));
	}

	public Set<T> getTransitionsFromStateToState(State from, State to) {
		TreeSet<T> toreturn = new TreeSet<T>(getTransitionsFromState(from));
		toreturn.retainAll(transitionsToStateMap.get(to));
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
		
		
		Set<T> toList = transitionsToStateMap.get(trans.getToState());
		if (toList == null){
			toList = new TreeSet<T>();
		}
		toList.add(trans);
		transitionsToStateMap.put(trans.getToState(), toList);
		
		return true;
	}

	@Override
	public boolean remove(Object trans){
		if (!this.remove(trans)) return false;
		transitionsFromStateMap.get(((T) trans).getFromState()).remove(trans);
		transitionsToStateMap.get(((T) trans).getToState()).remove(trans);
		return true;
	}

	public void purgeofInputSymbol(Symbol s){
		for (Transition t: this){
			t.getInput().purgeOfSymbol(s);
		}
	}
	
	@Override
	public void clear() {
		super.clear();
		transitionsFromStateMap.clear();
		transitionsToStateMap.clear();
	}


}
