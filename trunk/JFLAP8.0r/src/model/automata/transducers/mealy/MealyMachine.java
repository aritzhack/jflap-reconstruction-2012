package model.automata.transducers.mealy;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.automata.transducers.OutputAlphabet;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.Transducer;
import model.formaldef.components.FormalDefinitionComponent;

public class MealyMachine extends Transducer<MealyOutputFunction> {

	public MealyMachine(StateSet states, 
			InputAlphabet langAlph,
			OutputAlphabet outputAlph,
			TransitionFunctionSet<FiniteStateTransition> functions,
			StartState start,
			OutputFunctionSet<MealyOutputFunction> outputFunctions) {
		super(states, langAlph, outputAlph, functions, start, outputFunctions);
	}

	@Override
	public String getDescriptionName() {
		return "Mealy Machine";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}


}
