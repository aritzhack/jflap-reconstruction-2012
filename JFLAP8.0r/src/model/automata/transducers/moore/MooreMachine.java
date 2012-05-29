package model.automata.transducers.moore;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.fsa.FSTransition;
import model.automata.transducers.OutputAlphabet;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.Transducer;
import model.formaldef.components.FormalDefinitionComponent;

public class MooreMachine extends Transducer<MooreOutputFunction> {


	public MooreMachine(StateSet states, 
							InputAlphabet langAlph,
							OutputAlphabet outputAlph,
							TransitionSet<FSTransition> functions,
							StartState start,
							OutputFunctionSet<MooreOutputFunction> outputFunctions) {
		super(states, langAlph, outputAlph, functions, start, outputFunctions);
	}

	@Override
	public String getDescriptionName() {
		return "Moore Machine";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}


}
