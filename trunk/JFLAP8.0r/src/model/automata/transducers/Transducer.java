package model.automata.transducers;

import java.lang.reflect.InvocationTargetException;

import model.automata.Automaton;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.specific.InputAlphabet;
import model.formaldef.components.alphabets.specific.OutputAlphabet;

public abstract class Transducer extends Automaton {

	private OutputAlphabet myOutputAlphabet;

	public Transducer(StateSet states, 
					InputAlphabet langAlph,
					OutputAlphabet outputAlph,
					TransitionFunctionSet functions, 
					StartState start) {
		super(states, langAlph, functions, start);
		myOutputAlphabet = outputAlph;
	}

	@Override
	public FormalDefinition<InputAlphabet, TransitionFunctionSet> alphabetAloneCopy() {
		try {
			return (FormalDefinition<InputAlphabet, TransitionFunctionSet>) 
					this.getClass().getConstructors()[0].newInstance(new StartState(),
																	this.getInputAlphabet(),
																	this.getOutputAlphabet(),
																	new TransitionFunctionSet(),
																	new StartState());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public OutputAlphabet getOutputAlphabet() {
		return myOutputAlphabet;
	}
	
	@Override
	public FormalDefinitionComponent[] getComponents() {
		return new FormalDefinitionComponent[]{this.getStates(),
											this.getInputAlphabet(),
											this.getOutputAlphabet(),
											this.getTransitions(),
											this.getStartState()};
	}

}
