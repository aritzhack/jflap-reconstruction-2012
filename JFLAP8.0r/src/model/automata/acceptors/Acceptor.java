package model.automata.acceptors;

import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.Transition;
import model.automata.TransitionFunctionSet;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;

public abstract class Acceptor<T extends Transition> extends Automaton<T> {

	private FinalStateSet myFinalStates;

	public Acceptor(StateSet states, 
					InputAlphabet langAlph,
					TransitionFunctionSet<T> functions, 
					StartState start,
					FinalStateSet finalStates) {
		super(states, langAlph, functions, start);
		
		myFinalStates = finalStates;
	}
	
	public FinalStateSet getFinalStateSet(){
		return myFinalStates;
	}
	
	@Override
	public FormalDefinitionComponent[] getComponents() {
			return new FormalDefinitionComponent[]{this.getStates(),
												this.getInputAlphabet(),
												this.getTransitions(),
												this.getStartState(),
												this.getFinalStateSet()};
	}

}
