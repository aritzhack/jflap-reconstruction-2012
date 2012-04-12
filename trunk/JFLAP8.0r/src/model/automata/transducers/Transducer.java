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


	public Transducer(StateSet states, 
					InputAlphabet langAlph,
					OutputAlphabet outputAlph,
					TransitionFunctionSet<FiniteStateTransition> functions, 
					StartState start,
					OutputFunctionSet<T> outputFunctions) {
		super(states, langAlph, outputAlph, functions, start, outputFunctions);
	}

	@Override
	public Transducer alphabetAloneCopy() {
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
		return this.getComponentOfClass(OutputAlphabet.class);
	}
	
	public OutputFunctionSet<T> getOutputFunctionSet(){
		return this.getComponentOfClass(OutputFunctionSet.class);
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
