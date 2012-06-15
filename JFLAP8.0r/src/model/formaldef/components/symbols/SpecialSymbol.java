package model.formaldef.components.symbols;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;
import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.StartVariable;

public abstract class SpecialSymbol extends FormalDefinitionComponent implements UsesSymbols {

	private Symbol mySymbol;
	
	public SpecialSymbol(String s) {
		this (new Symbol(s));
	}

	public SpecialSymbol(Symbol s) {
		this.setTo(s);
	}
	
	
	public SpecialSymbol(){
		this((Symbol) null);
	}

	public void setTo(Symbol s) {
		mySymbol = s;
		distributeChange(SPECIAL_SYMBOL_SET, mySymbol);
	}

	public Symbol toSymbolObject() {
		return mySymbol;
	}
	
	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(mySymbol != null,
						"The " + this.getDescriptionName() + " must be set before you can continue");
	}
	
	@Override
	public String toString() {
		return this.getDescriptionName() + ": " + (mySymbol == null ? "" : mySymbol.toString());
	}

	@Override
	public SpecialSymbol copy() {
		try {
			return this.getClass().getConstructor(String.class).newInstance(this.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		return new TreeSet<Symbol>(Arrays.asList(new Symbol[]{mySymbol}));
	}

	@Override
	public boolean purgeOfSymbol(Alphabet a, Symbol s) {
		if (this.equals(s)){
			this.clear();
			return true;
		}
		return false;
	}

	public void clear() {
		this.setTo(null);
	}
	
	
}
