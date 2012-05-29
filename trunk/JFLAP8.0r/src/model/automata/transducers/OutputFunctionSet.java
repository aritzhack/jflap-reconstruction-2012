package model.automata.transducers;

import model.automata.acceptors.fsa.FSTransition;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.symbols.SymbolString;

public class OutputFunctionSet<T extends OutputFunction> extends FunctionSet<T> {

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

	public SymbolString getOuputForTransition(FSTransition trans) {
		for (T func: this){
			if (func.matches(trans))
				return func.getOutput();
		}
		return null;
	}

}
