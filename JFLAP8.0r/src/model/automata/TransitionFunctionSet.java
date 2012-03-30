package model.automata;

import model.formaldef.components.functionset.FunctionSet;

public class TransitionFunctionSet extends FunctionSet<Transition> {

	@Override
	public Character getCharacterAbbr() {
		return "\u03B4".charAt(0);
	}

	@Override
	public String getDescriptionName() {
		return "Transitions";
	}

	@Override
	public String getDescription() {
		return "The set of transition functions which" +
					"define the language.";
	}

}
