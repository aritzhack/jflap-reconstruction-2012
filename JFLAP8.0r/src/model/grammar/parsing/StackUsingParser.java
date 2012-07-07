package model.grammar.parsing;

import java.util.LinkedList;

import universe.preferences.JFLAPPreferences;

import model.grammar.Grammar;
import model.grammar.typetest.GrammarType;
import model.symbols.SymbolString;

public abstract class StackUsingParser extends Parser {

	private SymbolString myUnprocessedInput;
	
	public StackUsingParser(Grammar g) {
		super(g);
	}
	
	public abstract LinkedList getStack();
	
	public SymbolString getUnprocessedInput(){
		return myUnprocessedInput;
	}

	
	@Override
	public boolean resetParserStateOnly() {
		myUnprocessedInput = initUnprocessedInput();
		return true;
	}

	private SymbolString initUnprocessedInput() {
		SymbolString s = getInput().copy();
		s.add(JFLAPPreferences.getEndOfStringMarker());
		return s;
	}
	

}
