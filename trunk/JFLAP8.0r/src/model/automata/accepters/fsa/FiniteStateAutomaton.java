package model.automata.accepters.fsa;

import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.automata.accepters.Accepter;
import model.automata.accepters.FinalStateSet;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.specific.InputAlphabet;

public class FiniteStateAutomaton extends Accepter {

	public FiniteStateAutomaton(StateSet states, InputAlphabet langAlph,
			TransitionFunctionSet functions, StartState start,
			FinalStateSet finalStates) {
		super(states, langAlph, functions, start, finalStates);
	}

	@Override
	public String getDescriptionName() {
		return "Finite State Automaton (FSA)";
	}

	@Override
	public String getDescription() {
		return "A finite state automaton, basic accepter with no memory.";
	}

	@Override
	public FormalDefinition<InputAlphabet, TransitionFunctionSet> alphabetAloneCopy() {
		return new FiniteStateAutomaton(new StateSet(), 
						this.getInputAlphabet(), 
						new TransitionFunctionSet(), 
						new StartState(), 
						new FinalStateSet());
	}
	

}
