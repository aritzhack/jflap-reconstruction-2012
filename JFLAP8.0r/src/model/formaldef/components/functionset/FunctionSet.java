package model.formaldef.components.functionset;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;

import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.SetComponent;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.Symbol;

public abstract class FunctionSet<T extends LanguageFunction> extends SetComponent<T> implements UsesSymbols{

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(true);
	}


	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		
		TreeSet<Symbol> used = new TreeSet<Symbol>();
		
		for (LanguageFunction f: this){
			used.addAll(f.getUniqueSymbolsUsed());
		}
		return used;
	}
	
	
	@Override
	public boolean purgeOfSymbol(Symbol s) {
		boolean result = false;
		for (LanguageFunction f: this){
			result = f.purgeOfSymbol(s) || result;
		}
		return result;
	}
	
	
}
