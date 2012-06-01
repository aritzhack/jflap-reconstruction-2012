package model.grammar.parsing;

import util.Copyable;
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
	
	/**
	 * Returns true if the string was accepted.
	 * @param input
	 * @return
	 */
	public abstract boolean parse(SymbolString input);
	
	

}
