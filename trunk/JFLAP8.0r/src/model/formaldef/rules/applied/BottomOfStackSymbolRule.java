package model.formaldef.rules.applied;

import errors.BooleanWrapper;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.StackAlphabet;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.rules.AlphabetRule;

public class BottomOfStackSymbolRule extends AlphabetRule<StackAlphabet> {

	private BottomOfStackSymbol myBOSS;

	public BottomOfStackSymbolRule(BottomOfStackSymbol bottom) {
		myBOSS = bottom;
	}

	@Override
	public String getDescriptionName() {
		return "Bottom of Stack Symbol Rule";
	}

	@Override
	public String getDescription() {
		return "Ensures that, if there is a bottom of stack symbol, it cannot be" +
				"removed from the Stack Alphabet.";
	}

	@Override
	public BooleanWrapper canModify(StackAlphabet a, Symbol oldSymbol,
			Symbol newSymbol) {
		return new BooleanWrapper(checkDifferentFromBOSS(oldSymbol), "You may not modify the Bottom of Stack Symbol");
	}

	private boolean checkDifferentFromBOSS(Symbol oldSymbol) {
		return myBOSS.isComplete().isFalse() || !myBOSS.toSymbolObject().equals(oldSymbol);
	}

	@Override
	public BooleanWrapper canRemove(StackAlphabet a, Symbol oldSymbol) {
		return new BooleanWrapper(checkDifferentFromBOSS(oldSymbol), "You may not remove the Bottom of Stack Symbol" +
																		" from the Stack Alphabet");
	}

	@Override
	public BooleanWrapper canAdd(StackAlphabet a, Symbol newSymbol) {
		return new BooleanWrapper(true);
	}



}
