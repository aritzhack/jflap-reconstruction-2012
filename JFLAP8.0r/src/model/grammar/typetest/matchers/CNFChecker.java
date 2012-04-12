package model.grammar.typetest.matchers;

import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.grammar.Production;


public class CNFChecker extends ContextFreeChecker{

	@Override
	public boolean matchesRHS(SymbolString rhs) {
		return isSingleTerminal(rhs) || isDoubleVar(rhs);
	}

	private boolean isDoubleVar(SymbolString rhs) {
		return rhs.size() == 2 &&
				rhs.get(0) instanceof Variable &&
				rhs.get(1) instanceof Variable;
	}

	private boolean isSingleTerminal(SymbolString rhs) {
		return rhs.size() == 1 &&
				rhs.get(0) instanceof Terminal;
	}
	
}