package model.formaldef.rules;

import errors.BooleanWrapper;
import model.formaldef.alphabets.specific.TerminalAlphabet;
import model.formaldef.symbols.Symbol;
import model.grammar.Grammar;



public class TerminalsRule extends GrammarRule<Grammar, TerminalAlphabet> {

	@Override
	public BooleanWrapper canModify(Grammar parent, TerminalAlphabet a,
			Symbol oldSymbol, Symbol newSymbol) {
		return checkValid(parent.getVariableAlphabet(), newSymbol);
	}

	@Override
	public BooleanWrapper canAdd(Grammar parent, TerminalAlphabet a,
			Symbol newSymbol) {
		return checkValid(parent.getVariableAlphabet(), newSymbol);
	}

	
	@Override
	public String getDescription() {
		return "The Terminal Alphabet is simply that!";
	}

	@Override
	public String getName() {
		return "Terminals Rule";
	}

}
