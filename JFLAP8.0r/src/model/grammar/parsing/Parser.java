package model.grammar.parsing;

import util.Copyable;
import errors.BooleanWrapper;
import model.formaldef.Describable;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;


public abstract class Parser implements Describable, Copyable{


	protected Grammar myGrammar;

	public Parser(Grammar g){
		myGrammar = g;
	}

	public Grammar getGrammar(){
		return myGrammar;
	}
	
	public abstract BooleanWrapper init(SymbolString input);
	
	

}
