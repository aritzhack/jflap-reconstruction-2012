package model.formaldef.components.symbols;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;
import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.symbols.Symbol;

public class StartVariable extends SpecialSymbol implements FormalDefinitionComponent, UsesSymbols {

	public StartVariable(String s) {
		super(s);
	}

	public StartVariable() {
		this("");
	}

	@Override
	public String getName() {
		return "Start Variable";
	}

	@Override
	public String getDescription() {
		return "I am the start variable of the Grammar.";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'S';
	}

	
	
}
