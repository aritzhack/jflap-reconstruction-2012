/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */




package model.grammar.parsing.brute;

import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.parsing.ParserException;
import model.grammar.typetest.GrammarType;


/**
 * UnrestrictedUser Parser that is going to be used for parsing performed by user.
 * 
 * @author Kyung Min (Jason) Lee
 */
public class UnrestrictedUserParser extends UserParser{

	/**
	 * Creates a new unrestricted user parser.
	 * 
	 * @param grammar
	 *            the unrestricted grammar to parse
	 */
	public UnrestrictedUserParser(Grammar grammar) {
		super(grammar);
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public UnrestrictedUserParser copy() {
		return new UnrestrictedUserParser(getGrammar());
	}
	
	@Override
	public GrammarType getRequiredGrammarType() throws ParserException {
		return GrammarType.UNRESTRICTED;
	}
}
