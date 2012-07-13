package model.algorithms.testinput.parse.brute;

import java.util.*;

import debug.JFLAPDebug;
import model.grammar.*;
import model.algorithms.testinput.parse.*;
import model.algorithms.transform.grammar.UselessProductionRemover;
import model.change.events.AdvancedChangeEvent;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.typetest.GrammarType;
import model.grammar.typetest.matchers.ContextFreeChecker;

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

public class UnrestrictedBruteParser extends Parser {
	public static final int MAX_REACHED = 2;
	private int numberToGenerate = 10000;

	private Queue<Derivation> myDerivationsQueue;
	private int myNodesGenerated, maxLHSsize;
	private Derivation myAnswerDerivation;
	private Set<SymbolString> myPrederivedResults;
	private Set<Symbol> mySmallerSet;
	private boolean pause;
	
	public static UnrestrictedBruteParser createNewBruteParser(Grammar g){
		if(new ContextFreeChecker().matchesGrammar(g)){
			return new RestrictedBruteParser(g);
		}
		return new UnrestrictedBruteParser(optimize(g));
	}

	public UnrestrictedBruteParser(Grammar g) {
		super(g);
		mySmallerSet = Collections.unmodifiableSet(smallerSymbols(g));
		maxLHSsize = g.getProductionSet().getMaxLHSLength();
		
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
	public boolean resetInternalStateOnly() {
		myNodesGenerated = 0;
		pause = false;
		myPrederivedResults = new HashSet<SymbolString>();
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
		return isAccept() || myDerivationsQueue.isEmpty();
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
		return myNodesGenerated >= numberToGenerate;
	}

	@Override
	public boolean stepParser() {
		makeNextReplacement();
		if(capacityReached()){
			setPaused(true);
			distributeChange(new AdvancedChangeEvent(this, MAX_REACHED, numberToGenerate));
		}
		return true;
	}

	private void initializeQueue() {
		myDerivationsQueue = new LinkedList<Derivation>();
		Grammar g = getGrammar();
		Variable start = g.getStartVariable();
		
		for(Production p : g.getStartProductions()){
				Derivation d = new Derivation(p);
				myDerivationsQueue.add(d);
				myNodesGenerated++;
			
		}
	}
	
	public void setPaused(boolean pause){
		this.pause = pause;
	}

	/**
	 * Does the next level of parsing, adding all possible steps
	 * to each current possible derivation.
	 */
	private boolean makeNextReplacement() {
		ArrayList<Derivation> temp = new ArrayList<Derivation>();
		Grammar grammar = getGrammar();
		ProductionSet productions = grammar.getProductionSet();

		loop: while (!myDerivationsQueue.isEmpty()) {
			Derivation d = myDerivationsQueue.poll();
			SymbolString result = d.createResult();
			
			for (int i = 0; i < result.size(); i++) {
				for (int j = i; j < Math.min(maxLHSsize+i, result.size()); j++) {
					SymbolString LHS = result.subList(i, j+1);
					
					Production[] productionsWithLHS = productions.getProductionsWithLHS(LHS);
					
					for (Production p : productionsWithLHS) {
						Derivation tempDerivation = d.copy();
						int replacementIndex = result.indexOf(LHS, i);
						
						tempDerivation.addStep(p, replacementIndex);
						SymbolString tempResult = tempDerivation.createResult();
						
						if (isPossibleDerivation(tempResult)) {
							temp.add(tempDerivation);
							myNodesGenerated++;
							
							if (tempResult.equals(getInput())) {
								break loop;
							}
						}
					}
				}
			}
		}
		myDerivationsQueue.addAll(temp);
		return true;
	}

	public boolean raiseCapacity() {
		numberToGenerate *= 5;
		return true;
	}

	public int getNumberOfNodes() {
		return this.myNodesGenerated;
	}

	public boolean isPossibleDerivation(SymbolString derivation) {
		if(myPrederivedResults.contains(derivation)) return false;
		myPrederivedResults.add(derivation);
		Symbol[] derivationArray = derivation.toArray(new Symbol[0]);
		int min = minimumLength(derivationArray, mySmallerSet) ;
		return min <= getInput().size();
	}

	/**
	 * Given a string and a smaller set, this returns the minimum length that
	 * the string can derive as indicated by the smaller set.
	 * 
	 * @param right
	 *            the string to get the "smaller"
	 * @param smaller
	 *            the "smaller" set, as returned by {@link #smallerSymbols}
	 */
	public int minimumLength(Symbol[] right, Set<Symbol> smaller) {
		int length = 0;
		for (Symbol s: right)
			if (!smaller.contains(s))
				length++;
		return length;
	}

	/**
	 * Counts the number of characters in a given string.
	 * 
	 * @param left
	 *            the string
	 * @param c
	 *            the character
	 * @return the number of occurrences of the character in the string
	 */
	private int count(Symbol[] left, Symbol s) {
		int count = 0;
		for (int i = 0; i < left.length; i++)
			if (left[i].equals(s))
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
				Symbol[] left = prods[i].getLHS();
				Symbol[] right = prods[i].getRHS();
				
				int rightLength = minimumLength(right, smaller);
				int leftLength = minimumLength(left, smaller);
				
				if (leftLength > rightLength) {
					for (int j = 0; j < left.length; j++) {
						Symbol symbol = left[j];
						
						if (smaller.contains(symbol)||(count(left, symbol) <= count(right, symbol)))
							continue;
						
						smaller.add(symbol);
						added = true;
					}
				}
			}
		} while (added);
		return smaller;
	}
	
	@Override
	public boolean canStep() {
		return !isPaused() && super.canStep();
	}
	
	private static Grammar optimize(Grammar g){
		UselessProductionRemover remover = new UselessProductionRemover(g);
		remover.stepToCompletion();
		return remover.getTransformedDefinition();
	}

	public boolean isPaused() {
		return pause;
	}
}
