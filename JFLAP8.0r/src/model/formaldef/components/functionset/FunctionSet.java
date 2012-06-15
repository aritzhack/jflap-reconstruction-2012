package model.formaldef.components.functionset;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;

import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.Symbol;

public abstract class FunctionSet<T extends LanguageFunction<T>> extends SetComponent<T> implements UsesSymbols{

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(true);
	}


	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		
		TreeSet<Symbol> used = new TreeSet<Symbol>();
		
		for (LanguageFunction<T> f: this){
			used.addAll(f.getSymbolsUsedForAlphabet(a));
		}
		return used;
	}
	
	
	@Override
	public boolean purgeOfSymbol(Alphabet a, Symbol s) {
		boolean result = false;
		for (LanguageFunction<T> f: this){
			result = f.purgeOfSymbol(a, s) || result;
		}
		return result;
	}
	
	
}
