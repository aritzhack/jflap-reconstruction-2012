package model.automata.accepters;

import model.automata.Automaton;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.specific.InputAlphabet;

public abstract class Accepter extends Automaton {

	private FinalStateSet myFinalStates;

	public Accepter(StateSet states, 
					InputAlphabet langAlph,
					TransitionFunctionSet functions, 
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
