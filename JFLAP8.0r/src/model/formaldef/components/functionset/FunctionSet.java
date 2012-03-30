package model.formaldef.components.functionset;

import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;

import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.functionset.function.LanguageFunction;

public abstract class FunctionSet<T extends LanguageFunction> extends TreeSet<T> implements FormalDefinitionComponent,
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
	
	
	@Override
	public boolean purgeOfSymbol(Symbol s) {
		boolean result = false;
		for (LanguageFunction f: this){
			result = f.purgeOfSymbol(s) || result;
		}
		return result;
	}
	
	@Override
	public String toString() {
		
		return this.getDescriptionName() + ": " + super.toString();
	}
	
	
}
