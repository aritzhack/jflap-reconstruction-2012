package model.grammar;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;
import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.symbols.SpecialSymbol;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Variable;

public class StartVariable extends SpecialSymbol{

	public StartVariable(String s) {
		super(new Variable(s));
	}

	public StartVariable(Variable v) {
		super(v);
	}
	
	public StartVariable(){
		this ((Variable)null);
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
	public Variable toSymbolObject() {
		return (Variable) super.toSymbolObject();
	}

	@Override
	public void setTo(Symbol s) {
		if (s == null ) 
			super.setTo(s);
		else
			super.setTo(new Variable(s.getString()));
	}
	
	
	
}
