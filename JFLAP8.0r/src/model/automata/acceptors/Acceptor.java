package model.automata.acceptors;

import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;

public abstract class Acceptor<T extends Transition> extends Automaton<T> {


	public Acceptor(FormalDefinitionComponent ...comps) {
		super(comps);
	}
	
	public FinalStateSet getFinalStateSet(){
		return getComponentOfClass(FinalStateSet.class);
	}
	
	public static boolean isFinalState(Acceptor a, State s){
		return ((Acceptor) a).getFinalStateSet().contains(s);
	}
	

}
