package model.grammar.typetest.matchers;

import model.formaldef.components.alphabets.symbols.SymbolString;
import model.formaldef.components.alphabets.symbols.Terminal;
import model.formaldef.components.alphabets.symbols.Variable;

public class RightLinearChecker extends ContextFreeChecker {

	@Override
	public boolean matchesRHS(SymbolString rhs) {
		return rhs.isEmpty() || 
				checkLinear(rhs, Terminal.class, Variable.class, false) ||
				containsOnly(rhs, Terminal.class);
	}

}
