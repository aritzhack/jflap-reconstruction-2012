package model.automata.transducers.mealy;

import model.automata.State;
import model.automata.transducers.OutputFunction;
import model.formaldef.components.symbols.SymbolString;

public class MealyOutputFunction extends OutputFunction {

	public MealyOutputFunction(State s, SymbolString output) {
		super(s, output);
	}

	@Override
	public String getDescriptionName() {
		return "Mealy Output Function";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
