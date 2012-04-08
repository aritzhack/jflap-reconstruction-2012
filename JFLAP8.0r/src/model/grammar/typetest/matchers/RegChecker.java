package model.grammar.typetest.matchers;

import model.formaldef.components.alphabets.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;

public class RegChecker extends CNFChecker{

		private LeftLinearChecker myLeftLinearChecker;
		private RightLinearChecker myRightLinearChecker;

		public RegChecker() {
			myLeftLinearChecker = new LeftLinearChecker();
			myRightLinearChecker = new RightLinearChecker();
		}

		@Override
		public boolean matchesGrammar(Grammar g) {
			return myLeftLinearChecker.matchesGrammar(g) || 
					myRightLinearChecker.matchesGrammar(g);
		}

		@Override
		public boolean matchesRHS(SymbolString rhs) {
			return myLeftLinearChecker.matchesRHS(rhs) || 
					myRightLinearChecker.matchesRHS(rhs);
		}
		
		
		
	
		
		
	}