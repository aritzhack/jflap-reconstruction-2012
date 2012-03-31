package model.automata.transducers.moore;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.automata.transducers.OutputAlphabet;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.Transducer;

public class MooreMachine extends Transducer<MooreOutputFunction> {


	public MooreMachine(StateSet states, 
							InputAlphabet langAlph,
							OutputAlphabet outputAlph,
							TransitionFunctionSet<FiniteStateTransition> functions,
							StartState start,
							OutputFunctionSet<MooreOutputFunction> outputFunctions) {
		super(states, langAlph, outputAlph, functions, start, outputFunctions);
	}

	@Override
	public String getDescriptionName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
