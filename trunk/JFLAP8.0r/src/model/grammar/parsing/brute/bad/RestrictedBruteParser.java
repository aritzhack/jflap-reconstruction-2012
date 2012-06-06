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





package model.grammar.parsing.brute.bad;

import java.util.ArrayList;

import jflap.debug.JFLAPDebug;
import jflap.model.formaldef.symbols.Symbol;
import jflap.model.formaldef.symbols.SymbolString;
import jflap.model.formaldef.symbols.variable.Variable;
import jflap.model.grammar.Grammar;
import jflap.util.*;


/**
 * This is the brute parser for an restricted grammar. It has additional tests
 * for whether a derivation is possible.
 * 
 * @author Thomas Finley
 */

public class RestrictedBruteParser extends BruteParser {
	/**
	 * Creates a new unrestricted brute parser.
	 * 
	 * @param grammar
	 *            the unrestricted grammar to parse
	 */
	public RestrictedBruteParser(Grammar grammar) {
		super(grammar);
	}

	public boolean isPossibleDerivation(SymbolString derivation) {
		if (Unrestricted.minimumLength(derivation, mySmallerSet) > myTarget.size())
			return false;
		int targetSearched = 0;
		boolean startBookend = false, endBookend = false;
		ArrayList<SymbolString> discrete = new ArrayList<SymbolString>();
		int start = -1;

		/*
		 * Set the start and end "bookeneds", that is, the derivation is padded
		 * with terminals on either it's left or right sides.
		 */
		if (derivation.isEmpty()) {
			startBookend = endBookend = false;
		} else {
			startBookend = !Grammar.isVariable(derivation.getFirst());
			endBookend = !Grammar.isVariable(derivation.getLast());
		}

		/* Break up groups of terminals into the "discrete" array. */
		if (startBookend) discrete.add(new SymbolString());
		
		for (Symbol s: derivation) {
			if(Grammar.isVariable(s))
				discrete.add(new SymbolString());
			if(Grammar.isTerminal(s))
				discrete.get(discrete.size()-1).add(s);
		}
		
		if (!endBookend) discrete.remove(discrete.size()-1);
		
		
		int cp = 0;
		for (int i = 0; i < discrete.size(); i++) {
			SymbolString e = discrete.get(i);
			if (startBookend && i == 0) {
				if (!myTarget.startsWith(e)){
					JFLAPDebug.print(1);
					return false;
				}
				cp = e.size();
			} else if (endBookend && i == discrete.size() - 1) {
				if (!myTarget.endsWith(e)){
					JFLAPDebug.print(2);
					return false;
				}
			} else {
				cp = myTarget.indexOf(e, cp);
				if (cp == -1){
					JFLAPDebug.print(3);
					return false;
				}
				cp += e.size();
			}
		}
		return true;
	}
}
