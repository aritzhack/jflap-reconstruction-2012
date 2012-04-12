package model.grammar.typetest.matchers;

import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;

public class LeftLinearChecker extends ContextFreeChecker {

	@Override
	public boolean matchesRHS(SymbolString rhs) {
		return rhs.isEmpty() || 
				checkLinear(rhs, Variable.class, Terminal.class, true) ||
				containsOnly(rhs, Terminal.class);
	}

}
