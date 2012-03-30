package model.formaldef.rules;

import model.formaldef.components.alphabets.Alphabet;

public class SelfIdenticalRule<T extends Alphabet> extends IndividualIdenticalSymbolRule<T, T> {

	public SelfIdenticalRule(T alphabet) {
		super(alphabet);
	}

	@Override
	public String getDescriptionName() {
		return "Self-Identical Symbol Rule";
	}

	@Override
	public String getDescription() {
		return "Any single alphabet may not contain 2 identical symbols.";
	}

}
