package model.formaldef.rules;

import model.formaldef.components.alphabets.specific.TerminalAlphabet;
import model.formaldef.components.alphabets.specific.VariableAlphabet;

public class TermsVersusVarsIdenticalRule extends IndividualIdenticalSymbolRule<TerminalAlphabet, VariableAlphabet> {


	public TermsVersusVarsIdenticalRule(VariableAlphabet alphabet) {
		super(alphabet);
	}

	@Override
	public String getDescriptionName() {
		return "Terminal versus Variables Identical Rule";
	}

	@Override
	public String getDescription() {
		return "No terminal may be identical to any symbol in the Variable Alphabet";
	}

}
