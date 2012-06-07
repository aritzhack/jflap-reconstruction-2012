package model.grammar.parsing.brute;

import java.util.*;

import model.grammar.*;
import model.grammar.parsing.*;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.parsing.Derivation;
import model.grammar.parsing.Parser;
import model.grammar.parsing.ParserException;
import model.grammar.typetest.GrammarType;

/**
 * Brute force parser
 * 
 * Note: Brute force parser was re-implemented in Summer 2012 to conform with
 * the new parser hierarchy and new classes.
 * 
 * @author Peggy Li
 * @author Julian Genkins
 * @author Ian McMahon
 * 
 */

public abstract class BruteParser extends Parser {

	private static final int INCREMENT_CAPACITY_BY = 5000;
	private int NODES_TO_GENERATE = 1000000;

	private Queue<Derivation> myDerivationsQueue;
	private int myNodesGenerated, maxLHSsize;
	private Derivation myAnswerDerivation;
	protected Set<Symbol> mySmallerSet;

	public BruteParser(Grammar g) {
		super(g);
		mySmallerSet = Collections.unmodifiableSet(smallerSymbols(g));
		maxLHSsize = 0;
		for (Production p : g.getProductionSet()) {
			if (p.getLHS().size() > maxLHSsize) {
				maxLHSsize = p.getLHS().size();
			}
		}
	}

	@Override
	public String getDescriptionName() {
		return "Brute Force Parser";
	}

	@Override
	public String getDescription() {
		return "Brute force parsing implementation";
	}

	@Override
	public boolean resetParserStateOnly() {
		myNodesGenerated = 0;
		initializeQueue();
		return true;
	}

	@Override
	public boolean isAccept() {
		for (Derivation d : myDerivationsQueue) {
			if (d.createResult().equals(getInput())) {
				myAnswerDerivation = d;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDone() {
		return capacityReached() || isAccept() || myDerivationsQueue.isEmpty();
	}

	@Override
	public GrammarType getRequiredGrammarType() throws ParserException {
		return GrammarType.UNRESTRICTED;
	}

	@Override
	public Derivation getDerivation() {
		return myAnswerDerivation;
	}

	private boolean capacityReached() {
		return myNodesGenerated >= NODES_TO_GENERATE;
	}

	@Override
	public boolean stepParser() {
		makeNextReplacement();
		return true;
	}

	private void initializeQueue() {
		myDerivationsQueue = new LinkedList<Derivation>();

		SymbolString start = new SymbolString(getGrammar().getStartVariable());
		Derivation d = new Derivation(new Production(new SymbolString(), start));
		myDerivationsQueue.add(d);
		myNodesGenerated++;
	}

	private boolean makeNextReplacement() {

		ArrayList<Derivation> temp = new ArrayList<Derivation>();

		loop: while (!myDerivationsQueue.isEmpty()) {
			Derivation d = myDerivationsQueue.poll();
			SymbolString result = d.createResult();
			for (int i = 0; i < result.size(); i++) {
				for (int j = i; j < maxLHSsize + i; j++) {
					SymbolString LHS = result.subList(i, j + 1);
					for (Production p : getGrammar().getProductionSet()
							.getProductionsWithLHS(LHS)) {
						Derivation tempDerivation = d.copy();
						tempDerivation.addStep(p, result.indexOf(LHS, i));
						if (isPossibleDerivation(tempDerivation.createResult())) {
							temp.add(tempDerivation);
							myNodesGenerated++;
							if (tempDerivation.createResult()
									.equals(getInput())) {
								break loop;
							}

						}
					}
				}
			}
			if (myNodesGenerated >= NODES_TO_GENERATE) {
				break;
			}
		}
		myDerivationsQueue.addAll(temp);
		return true;
	}

	private boolean raiseCapacity() {
		NODES_TO_GENERATE += INCREMENT_CAPACITY_BY;
		return true;
	}

	public int getNumberOfNodes() {
		return this.myNodesGenerated;
	}

	public boolean isPossibleDerivation(SymbolString derivation) {
		return minimumLength(derivation, mySmallerSet) <= getInput()
				.size();
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
	public int minimumLength(SymbolString symbolString, Set<Symbol> smaller) {
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
	private int count(SymbolString str, Symbol s) {
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
	private Set<Symbol> smallerSymbols(Grammar grammar) {
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
}