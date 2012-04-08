package model.grammar.typetest.matchers;

import model.formaldef.Describable;
import model.formaldef.components.alphabets.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;



public abstract class GrammarChecker{
	
		public  boolean matchesGrammar(Grammar g){
			for (Production p: g.getProductionSet()){
				if (!this.matchesProduction(p))
					return false;
			}
			return true;
		}

		public boolean matchesProduction(Production p) {
			return this.matchesLHS(p.getLHS()) && this.matchesRHS(p.getRHS());
		}

		public abstract boolean matchesRHS(SymbolString rhs);

		public abstract boolean matchesLHS(SymbolString lhs);
		
}