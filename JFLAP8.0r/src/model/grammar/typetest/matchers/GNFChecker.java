package model.grammar.typetest.matchers;

import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;
import model.formaldef.components.alphabets.symbols.Terminal;
import model.formaldef.components.alphabets.symbols.Variable;
import model.grammar.Production;

public class GNFChecker extends ContextFreeChecker{

	@Override
	public boolean matchesRHS(SymbolString rhs) {
		return checkLinear(rhs, 
				Terminal.class, 
				Variable.class, 
				true);
	}


		
	}