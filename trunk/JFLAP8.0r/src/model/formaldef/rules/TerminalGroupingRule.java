package model.formaldef.rules;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.grammar.TerminalAlphabet;
import errors.BooleanWrapper;

public class TerminalGroupingRule extends GroupingRule<TerminalAlphabet> {

	public TerminalGroupingRule(GroupingPair gp) {
		super(gp);
	}

	@Override
	public String getDescriptionName() {
		return "Restrictions on Terminals in Grouping case";
	}

	@Override
	public String getDescription() {
		return "This rule prevents any symbols from being added to the terminal alphabet if " +
				"they contain any characters used for group in the Variable Alphabet.";
	}

	@Override
	public BooleanWrapper canModify(TerminalAlphabet a, Symbol oldSymbol,
			Symbol newSymbol) {
		return canAdd(a, newSymbol);
	}

	@Override
	public BooleanWrapper canAdd(TerminalAlphabet a, Symbol newSymbol) {
		return new BooleanWrapper(!containsGrouping(newSymbol), 
									"A Terminal cannot contain the characters currently " +
									"used for grouping in the Variable Alphabet - " + this.getGroupingPair());
	}

	private boolean containsGrouping(Symbol newSymbol) {
		return newSymbol.containsCharacters(this.getOpenGroup(), this.getCloseGroup());
	}


}
