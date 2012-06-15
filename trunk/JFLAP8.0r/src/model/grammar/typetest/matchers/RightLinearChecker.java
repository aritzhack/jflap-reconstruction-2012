package model.grammar.typetest.matchers;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;

public class RightLinearChecker extends ContextFreeChecker {

	@Override
	public boolean matchesRHS(Symbol[] rhs) {
		return rhs.length == 0 || 
				checkLinear(rhs, Terminal.class, Variable.class, false) ||
				containsOnly(rhs, Terminal.class);
	}

}
