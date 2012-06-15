package model.grammar.typetest.matchers;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Production;

public class UnrestrictedChecker extends GrammarChecker{

		@Override
		public boolean matchesRHS(Symbol[] rhs) {
			return true;
		}

		@Override
		public boolean matchesLHS(Symbol[] lhs) {
			return lhs.length != 0;
		}

	}