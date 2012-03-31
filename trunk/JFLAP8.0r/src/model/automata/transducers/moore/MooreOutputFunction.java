package model.automata.transducers.moore;

import model.automata.State;
import model.automata.transducers.OutputFunction;
import model.formaldef.components.alphabets.symbols.SymbolString;

public class MooreOutputFunction extends OutputFunction {

	public MooreOutputFunction(State s, SymbolString output) {
		super(s, output);
	}

	@Override
	public String getDescriptionName() {
		return "Moore OutputFunction";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
