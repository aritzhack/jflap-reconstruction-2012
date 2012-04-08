package model.grammar.typetest.matchers;

import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;
import model.formaldef.components.alphabets.symbols.Terminal;
import model.formaldef.components.alphabets.symbols.Variable;
import model.grammar.Production;


public class ContextFreeChecker extends GrammarChecker{


		@Override
		public boolean matchesRHS(SymbolString rhs) {
			return true;
		}

		@Override
		public boolean matchesLHS(SymbolString lhs) {
			return lhs.size() == 1 && lhs.getFirst() instanceof Variable;
		}
		
		
		
		/***
		 * Checks if a symbol string is of the "linear" form.
		 * This means that is its of form AB* or A*B where
		 * A is of class first, B is of class second.
		 * 
		 * The boolean first_dominant is used to show whether the
		 * first symbol (true) is the one alone or if the last 
		 * symbol (false) is the one alone.
		 * 
		 * @param ss
		 * @param first
		 * @param second
		 * @param first_dominant
		 * @return
		 */
		public static boolean checkLinear(SymbolString ss,
											Class<? extends Symbol> first,
											Class<? extends Symbol> second, 
											boolean first_dominant){
			
			if (ss.isEmpty()){
				return false;
			}
			
			if ((first_dominant && 
					!(first.isAssignableFrom(ss.getFirst().getClass()))) ||
					!(second.isAssignableFrom(ss.getLast().getClass()))){
				return false;
			}
			
			if (ss.size() == 1) return true;
			
			Class<? extends Symbol> repeated;
			SymbolString subString;
			
			if(first_dominant){
				repeated = second;
				subString = ss.subList(1);
			}
			else {
				repeated = first;
				subString = ss.subList(0, ss.size()-1);
			}
			
			return containsOnly(subString, repeated);
			
		}

		public static boolean containsOnly(SymbolString subString,
				Class<? extends Symbol> repeated) {
			
			for (Symbol symbol: subString){
				if (!(repeated.isAssignableFrom(symbol.getClass()))){ 
					return false;
				}
			}

			return true;
		}
}