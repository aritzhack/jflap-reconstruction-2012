package model.grammar.typetest.matchers;

import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;

public class RightLinearChecker extends ContextFreeChecker {

	@Override
	public boolean matchesRHS(SymbolString rhs) {
		return rhs.isEmpty() || 
				checkLinear(rhs, Terminal.class, Variable.class, false) ||
				containsOnly(rhs, Terminal.class);
	}

}
