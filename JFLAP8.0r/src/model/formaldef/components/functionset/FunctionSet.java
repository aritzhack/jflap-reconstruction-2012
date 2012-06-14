package model.formaldef.components.functionset;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;

import model.change.ChangeEvent;
import model.change.ChangeListener;
import model.change.ChangeDistributingObject;
import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.Symbol;
import model.grammar.Production;

public abstract class FunctionSet<T extends LanguageFunction<T>> extends SetComponent<T> implements UsesSymbols,
																									ChangeListener{

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(true);
	}


	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		
		TreeSet<Symbol> used = new TreeSet<Symbol>();
		
		for (LanguageFunction<T> f: this){
			used.addAll(f.getUniqueSymbolsUsed());
		}
		return used;
	}


}
