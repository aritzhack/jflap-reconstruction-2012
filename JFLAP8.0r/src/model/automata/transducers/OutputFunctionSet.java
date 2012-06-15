package model.automata.transducers;

import model.automata.acceptors.fsa.FSATransition;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.symbols.SymbolString;

public class OutputFunctionSet<T extends OutputFunction<T>> extends FunctionSet<T> {

	@Override
	public Character getCharacterAbbr() {
		return 'G';
	}

	@Override
	public String getDescriptionName() {
		return "Output Function Set";
	}

	@Override
	public String getDescription() {
		return "The set of functions that determine the output of a transducer.";
	}

	public SymbolString getOutputForTransition(FSATransition trans) {
		for (OutputFunction func: this){
			if (func.matches(trans))
				return func.getOutput();
		}
		return null;
	}

}
