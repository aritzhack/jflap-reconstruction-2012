package model.automata.transducers;

import java.lang.reflect.InvocationTargetException;

import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;

public abstract class Transducer<T extends OutputFunction> extends Automaton<FiniteStateTransition> {

	private OutputAlphabet myOutputAlphabet;
	private OutputFunctionSet<T> myOutputFunctions;

	public Transducer(StateSet states, 
					InputAlphabet langAlph,
					OutputAlphabet outputAlph,
					TransitionFunctionSet<FiniteStateTransition> functions, 
					StartState start,
					OutputFunctionSet<T> outputFunctions) {
		super(states, langAlph, functions, start);
		myOutputAlphabet = outputAlph;
		myOutputFunctions = outputFunctions;
	}

	@Override
	public FormalDefinition<InputAlphabet, TransitionFunctionSet<FiniteStateTransition>> alphabetAloneCopy() {
		Class<Transducer> clz = (Class<Transducer>) this.getClass();
		try {
					return clz.cast(clz.getConstructors()[0].newInstance(new StartState(),
																			this.getInputAlphabet(),
																			this.getOutputAlphabet(),
																			new TransitionFunctionSet<FiniteStateTransition>(),
																			new StartState(),
																			new OutputFunctionSet<T>()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public OutputAlphabet getOutputAlphabet() {
		return myOutputAlphabet;
	}
	
	public OutputFunctionSet<T> getOutputFunctionSet(){
		return myOutputFunctions;
	}
	
	@Override
	public FormalDefinitionComponent[] getComponents() {
		return new FormalDefinitionComponent[]{this.getStates(),
											this.getInputAlphabet(),
											this.getOutputAlphabet(),
											this.getTransitions(),
											this.getStartState(),
											this.getOutputFunctionSet()};
	}

}
