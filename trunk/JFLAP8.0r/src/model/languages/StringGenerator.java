package model.languages;

/**
 * Utility class for generating a set of strings
 * in a language given a restricted grammar 
 * for the language
 */

import java.util.*;

import model.algorithms.conversion.autotogram.*;
import model.automata.acceptors.fsa.*;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.formaldef.components.symbols.*;
import model.grammar.*;
import model.grammar.parsing.*;
import model.grammar.parsing.brute.*;
import model.grammar.parsing.cyk.CYKParser;
import model.grammar.parsing.ll.LL1Parser;
import model.grammar.transform.CNFConverter;
import model.grammar.typetest.matchers.*;

public class StringGenerator {

	private Grammar myGrammar;

	private Queue<Derivation> myDerivationsQueue;
	private Set<SymbolString> myStringsInLanguage, myPossibleStrings;

	private int myNumberToGenerate, currentStringLength, maxLHSsize;

	public StringGenerator(FiniteStateAcceptor fsa) {
		this(new FSAtoRegGrammarConversion(fsa).getConvertedGrammar());
	}

	public StringGenerator(PushdownAutomaton pda) {
		this(new PDAtoCFGConverter(pda).getConvertedGrammar());
	}

	public StringGenerator(Grammar g) {
		myGrammar = g;
		myNumberToGenerate = g.getTerminals().size();

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
		myDerivationsQueue.clear();
		myPossibleStrings.clear();
		myStringsInLanguage.clear();
	}

	public List<SymbolString> generateStringsBrute(){
		return generateStringsBrute(myGrammar.getTerminals().size());
	}
	public List<SymbolString> generateContextFreeStrings(){
		return generateContextFreeStrings(myGrammar.getTerminals().size());
	}
	
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

	public List<SymbolString> generateContextFreeStrings(int numberToGenerate) {
		clear();
		myNumberToGenerate = numberToGenerate;
		checkForCorrectParser();
		while (myStringsInLanguage.size() < myNumberToGenerate) {
			parseNextLengthStrings();
		}
		return generate();
	}

	private List<SymbolString> generate() {
		ArrayList<SymbolString> stringsList = new ArrayList<SymbolString>(
				myStringsInLanguage);
		Collections.sort(stringsList, new StringComparator());
		return stringsList;
	}
	
	public List<SymbolString> generateStringsOfLength(int n){
		clear();
		checkForCorrectParser();
		for (Symbol terminal : myGrammar.getTerminals()) {
			myPossibleStrings.add(new SymbolString(terminal));
		}
		for(int i=1;i<n;i++){
			getNextLengthStrings();
		}
		parsePossibleStrings();
		return generate();
	}

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
	
	private void parsePossibleStrings(){
		Parser parser;
		if(new LL1Checker().matchesGrammar(myGrammar)){
			parser = new LL1Parser(myGrammar);
		}
		else{
			parser = new CYKParser(myGrammar);
		}
		for (SymbolString string : myPossibleStrings) {
			if (parser.quickParse(string) && myStringsInLanguage.size() < myNumberToGenerate) {
				myStringsInLanguage.add(string);
			}
		}
	}
	
	private void checkForCorrectParser(){
		if(!new ContextFreeChecker().matchesGrammar(myGrammar)) throw new ParserException("The grammar is not Context-Free."+
				" Try the brute generation instead.");
			if(!new LL1Checker().matchesGrammar(myGrammar)){
				CNFConverter converter = new CNFConverter(myGrammar);
				converter.stepToCompletion();
				myGrammar = converter.getTransformedGrammar();
			}	
	}
	
	private class StringComparator implements Comparator<SymbolString> {

		@Override
		public int compare(SymbolString s1, SymbolString s2) {
			if (s1.size() != s2.size())
				return s1.size() - s2.size();
			return s1.compareTo(s2);
		}

	}
}
