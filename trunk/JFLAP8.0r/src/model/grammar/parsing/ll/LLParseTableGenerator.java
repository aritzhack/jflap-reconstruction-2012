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





package model.grammar.parsing.ll;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import model.grammar.parsing.Operations;

import jflap.model.grammar.Grammar;
import jflap.model.grammar.Production;
import jflap.util.*;


/**
 * This class generates {@link model.grammar.parsing.ll.LLParseTable}s for grammars.
 * 
 * @author Thomas Finley
 */

public class LLParseTableGenerator {
	/**
	 * Can't instantiate this bad boy sparky.
	 */
	private LLParseTableGenerator() {
	}

	/**
	 * Generates a parse table for a particular grammar.
	 * 
	 * @param grammar
	 *            the grammar for which a complete parse table should be
	 *            generated
	 */
	public static LLParseTable generate(Grammar grammar) {
		LLParseTable table = new LLParseTable(grammar);
		Map first = Operations.first(grammar), follow = Operations
				.follow(grammar);
		Production[] productions = grammar.getProductions();
		for (int i = 0; i < productions.length; i++) {
			String alpha = productions[i].getRHS();
			String A = productions[i].getLHS();
			Set firsts = Operations.first(first, alpha);
			Iterator it = firsts.iterator();
			while (it.hasNext()) {
				String a = (String) it.next();
				if (!a.equals(""))
					table.addEntry(A, a, alpha);
			}
			if (!firsts.contains(""))
				continue;
			Set follows = (Set) follow.get(A);
			it = follows.iterator();
			while (it.hasNext())
				table.addEntry(A, (String) it.next(), alpha);
		}
		return table;
	}
}
