package model.automata.acceptors.fsa;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;

public class FiniteStateAcceptor extends Acceptor<FiniteStateTransition> {

	public FiniteStateAcceptor(StateSet states, 
									InputAlphabet langAlph,
									TransitionFunctionSet<FiniteStateTransition> functions, 
									StartState start,
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
	public FiniteStateAcceptor alphabetAloneCopy() {
		return new FiniteStateAcceptor(new StateSet(), 
						this.getInputAlphabet(), 
						new TransitionFunctionSet<FiniteStateTransition>(), 
						new StartState(), 
						new FinalStateSet());
	}
	

}
