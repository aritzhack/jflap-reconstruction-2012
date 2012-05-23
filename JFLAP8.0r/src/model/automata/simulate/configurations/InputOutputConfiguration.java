package model.automata.simulate.configurations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.automata.transducers.OutputFunction;
import model.automata.transducers.Transducer;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public abstract class InputOutputConfiguration<S extends Transducer<T>, T extends OutputFunction> 
							extends SingleSecondaryConfiguration<S, FiniteStateTransition> {

	public InputOutputConfiguration(S a, State s, int pos, SymbolString input,
			SymbolString output) {
		super(a, s, pos, input, output);
	}

	private SymbolString getOutput() {
		return super.getSecondaryString();
	}


	@Override
	protected boolean isInFinalState() {
		return true;
	}

	@Override
	protected String getSecondaryName() {
		return "Output";
	}

	@Override
	protected int getPositionChange(FiniteStateTransition trans) {
		return getOutputForTransition(trans).size();
	}

	private SymbolString getOutputForTransition(
			FiniteStateTransition trans){
		return this.getAutomaton().getOutputFunctionSet().getOuputForTransition(trans);
	}

	@Override
	protected SymbolString createUpdatedSecondary(SymbolString secClone,
			FiniteStateTransition trans) {
		return secClone.concat(getOutputForTransition(trans));
	}

	
}
