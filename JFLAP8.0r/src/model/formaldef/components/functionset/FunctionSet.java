package model.formaldef.components.functionset;

import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;

import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.functionset.function.LanguageFunction;

public abstract class FunctionSet extends TreeSet<LanguageFunction> implements FormalDefinitionComponent,
																				UsesSymbols{

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(true);
	}

	@Override
	public FormalDefinitionComponent clone() {
		return (FormalDefinitionComponent) super.clone();
	}
	
	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		
		TreeSet<Symbol> used = new TreeSet<Symbol>();
		
		for (LanguageFunction f: this){
			used.addAll(f.getUniqueSymbolsUsed());
		}
		return used;
	}
	
}
