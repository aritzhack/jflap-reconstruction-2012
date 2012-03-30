package model.formaldef.components.symbols;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;
import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.grammar.StartVariable;

public abstract class SpecialSymbol extends Symbol implements FormalDefinitionComponent, UsesSymbols {

	
	
	public SpecialSymbol(String s) {
		super(s);
	}

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(!this.toString().isEmpty(),
						"The " + this.getDescriptionName() + "must be set before you can continue");
	}

	@Override
	public StartVariable clone() {
		return (StartVariable) super.clone();
	}

	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		return new TreeSet<Symbol>(Arrays.asList(new Symbol[]{this}));
	}

	@Override
	public boolean purgeOfSymbol(Symbol s) {
		if (this.equals(s)){
			this.clear();
			return true;
		}
		return false;
	}

	public void clear() {
		this.setString("");
	}
	
	
}
