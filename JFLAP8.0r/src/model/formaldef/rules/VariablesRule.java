package model.formaldef.rules;

import errors.BooleanWrapper;
import model.formaldef.alphabets.specific.VariableAlphabet;
import model.formaldef.symbols.Symbol;
import model.grammar.Grammar;


public class VariablesRule extends GrammarRule<Grammar, VariableAlphabet> {

	@Override
	public BooleanWrapper canModify(Grammar parent, VariableAlphabet a,
			Symbol oldSymbol, Symbol newSymbol) {
		return checkValid(parent.getTerminalAlphabet(), newSymbol);
	}

	@Override
	public BooleanWrapper canAdd(Grammar parent, VariableAlphabet a,
			Symbol newSymbol) {
		return checkValid(parent.getTerminalAlphabet(), newSymbol);
	}

	@Override
	public String getDescription() {
		return "Variables are fun!!";
	}

	@Override
	public String getName() {
		return "Variables Rule";
	}


}
