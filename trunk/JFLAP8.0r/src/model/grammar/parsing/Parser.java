package model.grammar.parsing;

import util.Copyable;
import model.formaldef.Describable;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.typetest.GrammarType;


public abstract class Parser implements Describable, Copyable{


	protected Grammar myGrammar;

	public Parser(Grammar g) throws ParserException{
		GrammarType type = this.getRequiredGrammarType();
		if (!type.matches(g))
			throw new ParserException("To use the " + this.getDescriptionName() +
					" the grammar must be in " + type.name);
		
		myGrammar = g;
		
	}

	public abstract GrammarType getRequiredGrammarType() throws ParserException;

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
