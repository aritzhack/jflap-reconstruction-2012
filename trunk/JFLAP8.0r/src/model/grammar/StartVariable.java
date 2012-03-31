package model.grammar;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;
import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.symbols.SpecialSymbol;

public class StartVariable extends SpecialSymbol implements FormalDefinitionComponent, UsesSymbols {

	public StartVariable(String s) {
		super(s);
	}

	public StartVariable() {
		super();
	}

	@Override
	public String getDescriptionName() {
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
	
	@Override
	public String toString() {
		return getDescriptionName() + ": " + super.toString();
	}
	
}
