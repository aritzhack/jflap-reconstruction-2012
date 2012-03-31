package model.automata.transducers;

import model.formaldef.components.functionset.FunctionSet;

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

}
