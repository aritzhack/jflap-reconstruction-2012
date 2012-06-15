package model.languages;

/**
 * Utility class for generating sentences
 * in a language given a grammar 
 * for the language
 * 
 * @author Ian McMahon
 */

import java.util.*;

import model.algorithms.conversion.autotogram.*;
import model.algorithms.conversion.regextofa.RegularExpressionToNFAConversion;
import model.automata.acceptors.fsa.*;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.formaldef.components.symbols.*;
import model.grammar.*;
import model.grammar.parsing.*;
import model.grammar.parsing.cyk.CYKParser;
import model.grammar.parsing.ll.LL1Parser;
import model.grammar.transform.CNFConverter;
import model.grammar.typetest.matchers.*;
import model.regex.RegularExpression;

public class StringGenerator {

	private Grammar myGrammar;
	private int LARGE_NUMBER = 100000;
	private final int DEFAULT_NUMBER = 10;
	private Queue<Derivation> myDerivationsQueue;
	private Set<SymbolString> myStringsInLanguage, myPossibleStrings;
	private Parser myParser;

	private int myNumberToGenerate, currentStringLength, maxLHSsize;

	public StringGenerator(FiniteStateAcceptor fsa) {
		FSAtoRegGrammarConversion converter = new FSAtoRegGrammarConversion(fsa);
		converter.stepToCompletion();
		initialize(converter.getConvertedGrammar());
	}

	public StringGenerator(PushdownAutomaton pda) {
		PDAtoCFGConverter converter = new PDAtoCFGConverter(pda);
		converter.stepToCompletion();
		initialize(converter.getConvertedGrammar());
	}
	
	public StringGenerator(RegularExpression regex){
		RegularExpressionToNFAConversion nfa = new RegularExpressionToNFAConversion(regex);
		nfa.stepToCompletion();
		FSAtoRegGrammarConversion converter = new FSAtoRegGrammarConversion(nfa.getCompletedNFA());
		converter.stepToCompletion();
		initialize(converter.getConvertedGrammar());
	}

	public StringGenerator(Grammar g) {
		initialize(g);
	}
	
	private void initialize(Grammar g){
		myGrammar = g;
		
		myDerivationsQueue = new LinkedList<Derivation>();
		myStringsInLanguage = new HashSet<SymbolString>();
		myPossibleStrings = new TreeSet<SymbolString>();

		SymbolString start = new SymbolString(myGrammar.getStartVariable());
		Derivation d = new Derivation(new Production(new SymbolString(), start));
		myDerivationsQueue.add(d);
		maxLHSsize = 0;
		for (Production p : g.getProductionSet()) {
			if (p.getLHS().size() > maxLHSsize) {
				maxLHSsize = p.getLHS().size();
			}
		}
	}

	private void clear() {
		currentStringLength = 0;
		myDerivationsQueue.clear();
		myPossibleStrings.clear();
		myStringsInLanguage.clear();
	}

	/**
	 * Generates the default number of strings (the number of terminals in the TerminalAlphabet)
	 * using a brute force method.
	 */
	public List<SymbolString> generateStringsBrute(){
		return generateStringsBrute(DEFAULT_NUMBER);
	}
	
	/**
	 * Generates the default number of strings (the number of terminals in the TerminalAlphabet)
	 * using either CYK or LL parsing.
	 */
	public List<SymbolString> generateContextFreeStrings(){
		return generateContextFreeStrings(DEFAULT_NUMBER);
	}
	
	/**
	 * Generates the specified number of sentences using a brute force algorithm.
	 * @param numberToGenerate
	 * 			the number of sentences/strings this method will return
	 */
	public List<SymbolString> generateStringsBrute(int numberToGenerate) {
		clear();
		myNumberToGenerate = numberToGenerate;
		myDerivationsQueue.add(new Derivation(new Production(
				new SymbolString(), new SymbolString(myGrammar
						.getStartVariable()))));
		while (myStringsInLanguage.size() < myNumberToGenerate) {
			makeNextReplacement();
		}

		return generate();
	}

	/**
	 * Generates the first <CODE>numberToGenerate</CODE> sentences/strings that this language can produce,
	 * using LL or CYK parsing.
	 * @param numberToGenerate
	 * 			the number of sentences/strings this method will return.
	 */
	public List<SymbolString> generateContextFreeStrings(int numberToGenerate) {
		clear();
		myNumberToGenerate = numberToGenerate;
		checkForCorrectParser();
		while (myStringsInLanguage.size() < myNumberToGenerate) {
			parseNextLengthStrings();
		}
		return generate();
	}

	/**
	 * Returns a sorted list consisting of every SymbolString that has been put into <CODE>myStringsInLanguage</CODE>.
	 * Sorts based on length, followed by the natural order of SymbolStrings.
	 */
	private List<SymbolString> generate() {
		ArrayList<SymbolString> stringsList = new ArrayList<SymbolString>(
				myStringsInLanguage);
		Collections.sort(stringsList, new StringComparator());
		return stringsList;
	}
	
	/**
	 * Generates every string of specified length that can be produced by the language.
	 * @param length
	 * 			the length of the strings desired.
	 */
	public List<SymbolString> generateStringsOfLength(int length){
		return generateStringsOfLength(length, LARGE_NUMBER);
	}
	
	/**
	 *  Generates up to numberToGenerate strings of the specified length that the language can produce.
	 * @param length
	 * 			the length of the strings desired.
	 * @param numberToGenerate
	 * 			the maximum number of strings to generate.
	 */
	public List<SymbolString> generateStringsOfLength(int length, int numberToGenerate){
		clear();
		myNumberToGenerate = numberToGenerate;
		checkForCorrectParser();
		for (Symbol terminal : myGrammar.getTerminals()) {
			myPossibleStrings.add(new SymbolString(terminal));
		}
		for(int i=1;i<length;i++){
			getNextLengthStrings();
		}
		parsePossibleStrings();
		return generate();
	}

	/**
	 * Brute force method that generates strings as it finds them, based on the BruteParser algorithm.
	 */
	private boolean makeNextReplacement() {
		ArrayList<Derivation> temp = new ArrayList<Derivation>();
		loop: while (!myDerivationsQueue.isEmpty()) {
			Derivation d = myDerivationsQueue.poll();
			SymbolString result = d.createResult();
			for (int i = 0; i < result.size(); i++) {
				for (int j = i; j < maxLHSsize + i; j++) {
					SymbolString LHS = result.subList(i, j + 1);
					for (Production p : myGrammar.getProductionSet()
							.getProductionsWithLHS(LHS)) {
						if (myStringsInLanguage.size() >= myNumberToGenerate)
							break loop;
						Derivation tempDerivation = d.copy();
						tempDerivation.addStep(p, result.indexOf(LHS, i));
						temp.add(tempDerivation);
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
	 * Tracks the length of strings to deal with passing in the correct strings to the parsers.
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
	 * Modifies <CODE>myPossibleStrings</CODE> to consist of every possible string of the next length by 
	 * adding each terminal to the end of each existing possibility.
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
	 * Using LL or CKY parsing (based on grammar type) checks every possible string and adds any strings that
	 * the language can produce to <CODE>myStringsInLanguage</CODE>.
	 */
	private void parsePossibleStrings(){
		for (SymbolString string : myPossibleStrings) {
			if(myStringsInLanguage.size() >= myNumberToGenerate) break;
				if (myParser.quickParse(string)) {
					myStringsInLanguage.add(string);
				}
		}
	}
	
	/**
	 * Converts a context free grammar to CNF if it is not in LL(1) form.
	 */
	private void checkForCorrectParser(){
		if(! new ContextFreeChecker().matchesGrammar(myGrammar)) throw new ParserException("The grammar is not Context-Free."+
				" Try the brute generation instead.");
			if(!new LL1Checker().matchesGrammar(myGrammar)){
				CNFConverter converter = new CNFConverter(myGrammar);
				converter.stepToCompletion();
				myGrammar = converter.getTransformedGrammar();
				myParser = new CYKParser(myGrammar);
			}	else{
				myParser = new LL1Parser(myGrammar);
			}
	}
	
	/**
	 * Comparator for sorting the returned ArrayList in the <CODE>generate()</CODE> method.
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
