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

import java.util.HashSet;
import java.util.Set;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;



/**
 * This class is a utility class for determining some facts about unrestricted
 * grammars. As structures equivalent in power to Turing machines, a brute force
 * parse of an unrestricted grammar may, in some situations, not be recognized.
 * 
 * @author Thomas Finley
 */

public class Unrestricted {
	/**
	 * Dang class aint for instantiation! Get along, lil doggie.
	 */
	private Unrestricted() {
	}

	/**
	 * Given a string and a smaller set, this returns the minimum length that
	 * the string can derive as indicated by the smaller set.
	 * 
	 * @param symbolString
	 *            the string to get the "smaller"
	 * @param smaller
	 *            the "smaller" set, as returned by {@link #smallerSymbols}
	 */
	public static int minimumLength(SymbolString symbolString, Set<Symbol> smaller) {
		int length = 0;
		for (Symbol s: symbolString)
			if (!smaller.contains(s))
				length++;
		return length;
	}

	/**
	 * Counts the number of characters in a given string.
	 * 
	 * @param str
	 *            the string
	 * @param c
	 *            the character
	 * @return the number of occurrences of the character in the string
	 */
	private static int count(SymbolString str, Symbol s) {
		int count = 0;
		for (int i = 0; i < str.size(); i++)
			if (str.get(i).equals(s))
				count++;
		return count;
	}

	/**
	 * Returns a set of those symbols in the grammar that can derive some string
	 * smaller than it. For a normal grammar, of course, this would be just
	 * those variables with, but for an unrestricted grammar this can include
	 * the symbol <I>b</I> and <I>c</I> where <I>babca -> aa</I> is a rule.
	 * <I>a</I> is not included because there are <I>a</I> terminals in the
	 * result.
	 * 
	 * @param grammar
	 *            the grammar to find the "small" symbols for
	 */
	public static Set<Symbol> smallerSymbols(Grammar grammar) {
		Set<Symbol> smaller = new HashSet<Symbol>();
		Production[] prods = grammar.getProductionSet().toArray();
		boolean added;
		do {
			added = false;
			for (int i = 0; i < prods.length; i++) {
				SymbolString left = prods[i].getLHS();
				SymbolString right = prods[i].getRHS();
				int rightLength = minimumLength(right, smaller);
				int leftLength = minimumLength(left, smaller);
				if (leftLength > rightLength) {
					for (int j = 0; j < left.size(); j++) {
						Symbol symbol = left.get(j);
						if (smaller.contains(symbol))
							continue;
						if (count(left, symbol) <= count(right, symbol))
							continue;
						smaller.add(symbol);
						added = true;
					}
				}
			}
		} while (added);
		return smaller;
	}

	/**
	 * Returns if a grammar is truly unrestricted.
	 * 
	 * @param grammar
	 *            the grammar to test
	 * @return if a grammar is unrestricted
	 */
	public static boolean isUnrestricted(Grammar grammar) {
		Production[] prods = grammar.getProductionSet().toArray();
		for (int i = 0; i < prods.length; i++)
			if (prods[i].getLHS().size() != 1)
				return true;
		return false;
	}

	/**
	 * Given an unrestricted grammar, this will return an unrestricted grammar
	 * with fewer productions that accepts the same language.
	 * 
	 * @param grammar
	 *            the input grammar
	 * @return a grammar with productions some subset of the original grammar,
	 *         or <CODE>null</CODE> in the special case where no production
	 *         with just the start variable on the LHS exists (i.e. the grammar
	 *         accepts no language, though if a grammar accepts no language this
	 *         method is NOT gauranteed to return <CODE>null</CODE>)
	 */
	public static Grammar optimize(Grammar grammar) {
		Variable startVariable = grammar.getStartVariable();
		Production[] prods = grammar.getProductionSet().toArray();
		// Which symbols in the grammar may possibly lead to just
		// terminals? First, we just add all those symbols with just
		// terminals on the right hand side.
		Set<Symbol> terminating = new HashSet<Symbol>();
		// Add those variables that lead to success.
		boolean[] added = new boolean[prods.length];
		for (int i = 0; i < prods.length; i++) {
			added[i] = false;
			if (prods[i].getVariablesOnRHS().size() == 0) {
				terminating.addAll(prods[i].getUniqueSymbolsUsed());
				added[i] = true;
			}
		}
		// Repeat
		boolean changed;
		do {
			changed = false;
			// If a production has only "terminating" variables, add it.
			for (int i = 0; i < prods.length; i++) {
				Set<Variable> l = prods[i].getVariablesOnRHS();
				if (!added[i] && terminating.containsAll(l)) {
					terminating.addAll(prods[i].getUniqueSymbolsUsed());
					added[i] = changed = true;
				}
			}
		} while (changed);
		Grammar g = (Grammar) grammar.alphabetAloneCopy();
		g.setStartVariable(grammar.getStartVariable());
		// Need to find a production with just the start var on LHS.
		int i;
		for (i = 0; i < prods.length; i++)
			if (added[i] && prods[i].getLHS().equals(startVariable))
				break;
		if (i == prods.length)
			return null;
		
//		added[i] = g.isValidProduction(prods[i]);
		for (i = 0; i < prods.length; i++)
			if (added[i])
				g.getProductionSet().add(prods[i]);
		return g;
	}
}
