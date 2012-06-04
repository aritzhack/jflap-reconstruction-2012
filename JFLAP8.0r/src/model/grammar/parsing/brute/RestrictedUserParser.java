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


import java.util.ArrayList;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.parsing.ParserException;
import model.grammar.typetest.GrammarType;


/**
 * RestrictedUser Parser 
 * (This class is very similar to RestrictedBruteParser except for that this class extends UserParser) 
 * May be important in the future 
 * 
 * @author Kyung Min (Jason) Lee 
 */
public class RestrictedUserParser extends UserParser{
	
	/**
	 * Creates a new unrestricted brute parser.
	 * 
	 * @param grammar
	 *            the unrestricted grammar to parse
	 */
	public RestrictedUserParser(Grammar grammar) {
		super(grammar);
	}

	public boolean isPossibleDerivation(SymbolString derivation) {
		if (Unrestricted.minimumLength(derivation, mySmallerSet) > myTarget.size())
			return false;
		int targetSearched = 0;
		boolean startBookend = false, endBookend = false;
		ArrayList<SymbolString> discrete = new ArrayList<SymbolString>();
		StringBuffer sb = new StringBuffer();
		int start = -1;

		/*
		 * Set the start and end "bookeneds", that is, the derivation is padded
		 * with terminals on either it's left or right sides.
		 */
		if (derivation.isEmpty()) {
			startBookend = endBookend = false;
		} else {
			startBookend = Grammar.isVariable(derivation.getFirst());
			endBookend = Grammar.isVariable(derivation.getLast());
		}

		/* Break up groups of terminals into the "discrete" array. */
		for (int i = 0; i <= derivation.size(); i++) {
			Symbol symbol = (i == derivation.size()) ? null : derivation.get(i);
			if (symbol == null || Grammar.isVariable(symbol)) {
				// if (symbol == null) endBookend = true;
				if (sb.length() == 0)
					continue;
				if (start == -1)
					continue;
				discrete.add(derivation.subList(start, i));
				start = -1;
			} else if (Grammar.isTerminal(symbol)) {
				if (start == -1)
					start = i;
				sb.append(symbol);
				// if (i==0) startBookend = true;
			}
		}
		int cp = 0;
		for (int i = 0; i < discrete.size(); i++) {
			SymbolString e = discrete.get(i);
			if (startBookend && i == 0) {
				if (!myTarget.startsWith(e))
					return false;
				cp = e.size();
			} else if (endBookend && i == discrete.size() - 1) {
				if (!myTarget.endsWith(e))
					return false;
			} else {
				cp = myTarget.indexOf(e, cp);
				if (cp == -1)
					return false;
				cp += e.size();
			}
		}
		return true;
	}


	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public RestrictedUserParser copy() {
		return new RestrictedUserParser(getGrammar());
	}
	
	@Override
	public GrammarType getRequiredGrammarType() throws ParserException {
		return GrammarType.CONTEXT_FREE;
	}
}
