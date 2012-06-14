package model.grammar;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

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
	public boolean setTo(Symbol s) {
		if (s == null ) 
			return super.setTo(s);
		else
			return super.setTo(new Variable(s.getString()));
		
	}
	
	@Override
	public StartVariable copy() {
		return (StartVariable) super.copy();
	}
	
}
