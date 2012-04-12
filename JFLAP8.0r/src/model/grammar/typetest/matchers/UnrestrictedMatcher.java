package model.grammar.typetest.matchers;

import model.formaldef.components.symbols.SymbolString;
import model.grammar.Production;

public class UnrestrictedMatcher extends GrammarChecker{

		@Override
		public boolean matchesRHS(SymbolString rhs) {
			return true;
		}

		@Override
		public boolean matchesLHS(SymbolString lhs) {
			return !lhs.isEmpty();
		}

	}