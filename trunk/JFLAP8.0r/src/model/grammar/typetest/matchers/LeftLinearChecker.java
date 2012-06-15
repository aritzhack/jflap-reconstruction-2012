package model.grammar.typetest.matchers;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;

public class LeftLinearChecker extends ContextFreeChecker {

	@Override
	public boolean matchesRHS(Symbol[] rhs) {
		return rhs.length == 0 || 
				checkLinear(rhs, Variable.class, Terminal.class, true) ||
				containsOnly(rhs, Terminal.class);
	}

}
