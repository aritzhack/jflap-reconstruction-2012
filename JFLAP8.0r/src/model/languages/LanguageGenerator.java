package model.languages;

/**
 * Class used for generating sentences
 * in a language given a grammar 
 * for the language
 * 
 * @author Ian McMahon
 */

import java.util.*;

import debug.JFLAPDebug;

import model.algorithms.AlgorithmException;
import model.algorithms.conversion.autotogram.*;
import model.algorithms.conversion.regextofa.RegularExpressionToNFAConversion;
import model.algorithms.testinput.parse.*;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.algorithms.transform.grammar.CNFConverter;
import model.algorithms.transform.grammar.ConstructDependencyGraph;
import model.algorithms.transform.grammar.DependencyGraph;
import model.automata.acceptors.fsa.*;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.grammar.*;
import model.grammar.typetest.matchers.*;
import model.regex.RegularExpression;
import model.symbols.*;

public class LanguageGenerator {

	private Grammar myGrammar;
	private int LARGE_NUMBER = 100000;
	private final int DEFAULT_NUMBER = 10;
	private Queue<Derivation> myDerivationsQueue;
	private Set<SymbolString> myStringsInLanguage, myPossibleStrings;
	private Parser myParser;

	private int myNumberToGenerate, currentStringLength, maxLHSsize;

	public LanguageGenerator(FiniteStateAcceptor fsa) {
		FSAtoRegGrammarConversion converter = new FSAtoRegGrammarConversion(fsa);
		converter.stepToCompletion();
		initialize(converter.getConvertedGrammar());
	}

	public LanguageGenerator(PushdownAutomaton pda) {
		PDAtoCFGConverter converter = new PDAtoCFGConverter(pda);
		converter.stepToCompletion();
		initialize(converter.getConvertedGrammar());
	}

	public LanguageGenerator(RegularExpression regex) {
		RegularExpressionToNFAConversion nfa = new RegularExpressionToNFAConversion(
				regex);
		nfa.stepToCompletion();
		FSAtoRegGrammarConversion converter = new FSAtoRegGrammarConversion(
				nfa.getCompletedNFA());
		converter.stepToCompletion();
		initialize(converter.getConvertedGrammar());
	}

	public LanguageGenerator(Grammar g) {
		initialize(g);
	}

	private void initialize(Grammar g) {
		myGrammar = g;

		myDerivationsQueue = new LinkedList<Derivation>();
		myStringsInLanguage = new HashSet<SymbolString>();
		myPossibleStrings = new TreeSet<SymbolString>();

		SymbolString start = new SymbolString(myGrammar.getStartVariable());
		Derivation d = new Derivation(new Production(new SymbolString(), start));
		myDerivationsQueue.add(d);
		maxLHSsize = myGrammar.getProductionSet().getMaxLHSLength();
	}

	private void clear() {
		currentStringLength = 0;
		myDerivationsQueue.clear();
		myPossibleStrings.clear();
		myStringsInLanguage.clear();
	}

	public List<SymbolString> generateStrings(int numberToGenerate) {
		if (new ContextFreeChecker().matchesGrammar(myGrammar))
			if (!isGrammarFinite())
				return generateContextFreeStrings(numberToGenerate);
		return generateStringsBrute(numberToGenerate);
	}

	/**
	 * Generates the specified number of sentences using a brute force
	 * algorithm.
	 * 
	 * @param numberToGenerate
	 *            the number of sentences/strings this method will return
	 */
	private List<SymbolString> generateStringsBrute(int numberToGenerate) {
		clear();
		myNumberToGenerate = numberToGenerate;
		for (Production p : myGrammar.getStartProductions())
			myDerivationsQueue.add(new Derivation(p));
		while (myStringsInLanguage.size() < myNumberToGenerate
				&& !myDerivationsQueue.isEmpty()) {
			makeNextReplacement();
		}
		return generate();
	}

	/**
	 * Generates the first <CODE>numberToGenerate</CODE> sentences/strings that
	 * this language can produce, using CYK parsing.
	 * 
	 * @param numberToGenerate
	 *            the number of sentences/strings this method will return.
	 */
	private List<SymbolString> generateContextFreeStrings(int numberToGenerate) {
		clear();
		myNumberToGenerate = numberToGenerate;
		checkForCorrectParser();
		while (myStringsInLanguage.size() < myNumberToGenerate) {
			parseNextLengthStrings();
		}
		return generate();
	}

	/**
	 * Returns a sorted list consisting of every SymbolString that has been put
	 * into <CODE>myStringsInLanguage</CODE>. Sorts based on length, followed by
	 * the natural order of SymbolStrings.
	 */
	private List<SymbolString> generate() {
		ArrayList<SymbolString> stringsList = new ArrayList<SymbolString>(
				myStringsInLanguage);
		Collections.sort(stringsList, new StringComparator());
		return stringsList;
	}

	/**
	 * Generates every string of specified length that can be produced by the
	 * language.
	 * 
	 * @param length
	 *            the length of the strings desired.
	 */
	public List<SymbolString> generateStringsOfLength(int length) {
		return generateStringsOfLength(length, LARGE_NUMBER);
	}

	/**
	 * Generates up to numberToGenerate strings of the specified length that the
	 * language can produce.
	 * 
	 * @param length
	 *            the length of the strings desired.
	 * @param numberToGenerate
	 *            the maximum number of strings to generate.
	 */
	public List<SymbolString> generateStringsOfLength(int length,
			int numberToGenerate) {
		clear();
		myNumberToGenerate = numberToGenerate;
		checkForCorrectParser();
		for (Symbol terminal : myGrammar.getTerminals()) {
			myPossibleStrings.add(new SymbolString(terminal));
		}
		for (int i = 1; i < length; i++) {
			getNextLengthStrings();
		}
		parsePossibleStrings();
		return generate();
	}

	/**
	 * Brute force method that generates strings as it finds them, based on the
	 * BruteParser algorithm.
	 */
	private boolean makeNextReplacement() {
		ArrayList<Derivation> temp = new ArrayList<Derivation>();
		loop: while (!myDerivationsQueue.isEmpty()) {
			Derivation d = myDerivationsQueue.poll();
			SymbolString result = d.createResult();
			JFLAPDebug.print(result);
			
			for (int i = 0; i < result.size(); i++) {
				for (int j = i; j < maxLHSsize + i; j++) {
					SymbolString LHS = result.subList(i, j + 1);
					for (Production p : myGrammar.getProductionSet()
							.getProductionsWithLHS(LHS)) {

						Derivation tempDerivation = d.copy();
						tempDerivation.addStep(p, result.indexOf(LHS, i));
						temp.add(tempDerivation);
						JFLAPDebug.print(temp);
						if (tempDerivation.createResult()
								.getSymbolsOfClass(Variable.class).size() == 0) {
							myStringsInLanguage.add(tempDerivation
									.createResult());
						}
					}
				}
			}
		}
		myDerivationsQueue.addAll(temp);
		return true;
	}

	/**
	 * Tracks the length of strings to deal with passing in the correct strings
	 * to the parsers.
	 */
	private void parseNextLengthStrings() {
		if (currentStringLength > 0) {
			if (currentStringLength == 1) {
				for (Symbol terminal : myGrammar.getTerminals()) {
					myPossibleStrings.add(new SymbolString(terminal));
				}
			} else {
				getNextLengthStrings();
			}
			parsePossibleStrings();
		}
		currentStringLength++;
	}

	/**
	 * Modifies <CODE>myPossibleStrings</CODE> to consist of every possible
	 * string of the next length by adding each terminal to the end of each
	 * existing possibility.
	 */
	private void getNextLengthStrings() {
		List<SymbolString> tempList = new ArrayList<SymbolString>();
		tempList.addAll(myPossibleStrings);
		for (SymbolString string : tempList) {
			for (Symbol terminal : myGrammar.getTerminals()) {
				SymbolString temp = string.copy();
				temp.add(terminal);
				myPossibleStrings.add(temp);
			}
			myPossibleStrings.remove(string);
		}
	}

	/**
	 * Using CYK parsing, checks every possible string and adds any strings that
	 * the language can produce to <CODE>myStringsInLanguage</CODE>.
	 */
	private void parsePossibleStrings() {
		for (SymbolString string : myPossibleStrings) {
			if (myStringsInLanguage.size() >= myNumberToGenerate)
				break;
			if (myParser.quickParse(string)) {
				myStringsInLanguage.add(string);
			}
		}
	}

	/**
	 * Converts a context free grammar to CNF.
	 */
	private void checkForCorrectParser() {
		if (!new ContextFreeChecker().matchesGrammar(myGrammar)) {
			throw new AlgorithmException(
					"The grammar must be context free to specify a length");
		}
		for (Production p : myGrammar.getStartProductions()) {
			SymbolString lambda = new SymbolString();
			if (p.isLambdaProduction() && !myStringsInLanguage.contains(lambda))
				myStringsInLanguage.add(lambda);
		}
		CNFConverter converter = new CNFConverter(myGrammar);
		converter.stepToCompletion();
		myGrammar = converter.getTransformedGrammar();
		myParser = new CYKParser(myGrammar);

	}

	private boolean isGrammarFinite() {
		// checks loops from each variable to itself
		for (Production p : myGrammar.getProductionSet())
			if (p.getVariablesOnRHS().contains(p.getLHS()[0]))
				return false;
		ConstructDependencyGraph construct = new ConstructDependencyGraph(
				myGrammar);
		construct.stepToCompletion();
		DependencyGraph graph = construct.getDependencyGraph();

		for (Symbol v : myGrammar.getVariables()) {
			if (loopExists(graph, v, new ArrayList<Symbol>()))
				return false;
		}
		return true;
	}

	private boolean loopExists(DependencyGraph graph, Symbol v,
			List<Symbol> history) {
		if (history.contains(v))
			return true;
		history.add(v);
		Variable[] dependents = graph.getAllDependencies((Variable) v);
		for (Variable depend : dependents) {
			if (loopExists(graph, depend, history))
				return true;
		}
		history.remove(v);
		return false;
	}

	/**
	 * Comparator for sorting the returned ArrayList in the
	 * <CODE>generate()</CODE> method.
	 */
	private class StringComparator implements Comparator<SymbolString> {

		@Override
		public int compare(SymbolString s1, SymbolString s2) {
			if (s1.size() != s2.size())
				return s1.size() - s2.size();
			return s1.compareTo(s2);
		}

	}
}
