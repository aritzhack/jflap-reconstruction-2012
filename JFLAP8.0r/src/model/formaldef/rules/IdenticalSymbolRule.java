package model.formaldef.rules;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.symbols.Symbol;
import errors.BooleanWrapper;

public abstract class IdenticalSymbolRule<T extends Alphabet> extends AlphabetRule<T> {

	private Alphabet[] myAlphabets;

	public IdenticalSymbolRule(Alphabet ... alphabets) {
		myAlphabets = alphabets;
	}
	
	@Override
	public BooleanWrapper canModify(T a, Symbol oldSymbol,
			Symbol newSymbol) {
		
		SymbolConflict conflict = findConflict(newSymbol);
		
		boolean canModify = conflict == null || conflict.symbol.equals(oldSymbol);
		
		return new BooleanWrapper(canModify, "The symbol " + oldSymbol + " cannot be modified to " + 
												newSymbol + " because the latter is too " +
												"similar to the symbol " + conflict.symbol + " in the " + 
												conflict.alphabet.getDescriptionName() + ".");
	}

	@Override
	public BooleanWrapper canRemove(T a, Symbol oldSymbol) {
		return new BooleanWrapper(true);
	}

	@Override
	public BooleanWrapper canAdd(T a, Symbol newSymbol) {
		
		SymbolConflict conflict = findConflict(newSymbol);
		return new BooleanWrapper(conflict == null, 
									"The symbol " + newSymbol + " cannot be added to the " + 
									a.getDescriptionName() + " because it is too " +
									"similar to the symbol " + conflict.symbol + " in the " + 
									conflict.alphabet.getDescriptionName() + ".");
	}

	private SymbolConflict findConflict(Symbol newSymbol) {
		for (Alphabet alph: myAlphabets){
			for (Symbol s: alph){
				if (tooSimilar(s, newSymbol))
					return new SymbolConflict(alph, s);
			}
		}
		return null;
	}

	
	private boolean tooSimilar(Symbol s, Symbol newSymbol) {
		return s.equals(newSymbol);
	}

	private class SymbolConflict{
		private Alphabet alphabet;
		private Symbol symbol;
		
		public SymbolConflict(Alphabet a, Symbol s) {
			alphabet = a;
			symbol = s;
		}
	}

}
