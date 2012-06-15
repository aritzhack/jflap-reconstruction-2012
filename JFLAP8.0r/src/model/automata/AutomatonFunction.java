package model.automata;

import java.util.Set;
import java.util.TreeSet;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public abstract class AutomatonFunction<T extends AutomatonFunction<T>> implements LanguageFunction<T>{

	public AutomatonFunction() {
		super();
	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		TreeSet<Symbol> symbols = new TreeSet<Symbol>();
		for(SymbolString str: this.getPartsForAlphabet(a))
			symbols.addAll(str);
		return symbols;
	}

	@Override
	public boolean purgeOfSymbol(Alphabet a, Symbol s) {
		boolean removed = false;
		for(SymbolString str: this.getPartsForAlphabet(a)){
			if(str.removeEach(s))
				removed = true;
		}
		return removed;
	}

	public abstract SymbolString[] getPartsForAlphabet(Alphabet a);

}