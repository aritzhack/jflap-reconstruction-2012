package model.formaldef.rules;

import model.formaldef.components.alphabets.specific.TerminalAlphabet;
import model.formaldef.components.alphabets.specific.VariableAlphabet;

public class VarsVersusTermsIdenticalRule extends IndividualIdenticalSymbolRule<VariableAlphabet, TerminalAlphabet> {

	public VarsVersusTermsIdenticalRule(TerminalAlphabet alphabet) {
		super(alphabet);
	}

	@Override
	public String getDescriptionName() {
		return "Variable versus Terminal Identical Rule";
	}

	@Override
	public String getDescription() {
		return "No variable may be identical to any symbol in the Terminal Alphabet";
	}

}
