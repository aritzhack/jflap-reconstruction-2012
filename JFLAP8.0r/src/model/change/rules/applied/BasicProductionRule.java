package model.change.rules.applied;

import debug.JFLAPDebug;
import errors.BooleanWrapper;
import model.change.events.SetToEvent;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionException;

public class BasicProductionRule extends LanguageFunctionRule<Production> {

	@Override
	public String getDescriptionName() {
		return "Basic Production Rule";
	}

	@Override
	public String getDescription() {
		return "Checks to see that production contains all variables and/or terminals based" +
				" on the class of symbols, not the alphabet contents.";
	}

	@Override
	public BooleanWrapper checkRule(Production p) {
		SymbolString lhs = p.getLHS();
		SymbolString rhs = p.getRHS();
		
		return BooleanWrapper.combineWrappers(checkBadSymbols(lhs), 
													checkBadSymbols(rhs));
		
	}
	
	private BooleanWrapper checkBadSymbols(SymbolString side) {
		return new BooleanWrapper(!containsBadSymbol(side),"The SymbolString set as the LHS or RHS " +
					"in a production cannot contain non-terminal/non-variable " +
					"symbols.");
	}

	private boolean containsBadSymbol(SymbolString side) {
		for (Symbol s: side){
			if (!(Grammar.isTerminal(s) ||
					Grammar.isVariable(s)))
				return true;
		}
		return false;
	}


}
