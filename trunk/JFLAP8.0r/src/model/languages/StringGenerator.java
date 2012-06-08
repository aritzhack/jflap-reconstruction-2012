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
import model.grammar.parsing.Derivation;
import model.grammar.parsing.brute.*;

public class StringGenerator {

	public static final int DEFAULT_NUMBER_TO_GENERATE = 5;

	private Grammar myGrammar;

	private Queue<Derivation> myDerivationsQueue;
	private Set<SymbolString> myStringsInLanguage, myPossibleStrings;

	private int myNumberToGenerate, currentStringLength,maxLHSsize;

	public StringGenerator(Grammar g) {
		this(g, DEFAULT_NUMBER_TO_GENERATE);
	}

	public StringGenerator(FiniteStateAcceptor fsa) {
		this(new FSAtoRegGrammarConversion(fsa).getConvertedGrammar());
	}

	public StringGenerator(PushdownAutomaton pda) {
		this(new PDAtoCFGConverter(pda).getConvertedGrammar());
	}

	public StringGenerator(Grammar g, int numberToGenerate) {
		myGrammar = g;
		myNumberToGenerate = numberToGenerate;

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
	
	public void clear(){
		myDerivationsQueue.clear();
		myDerivationsQueue.add(new Derivation(new Production(new SymbolString(), new SymbolString(myGrammar.getStartVariable()))));
		myPossibleStrings.clear();
		myStringsInLanguage.clear();
	}

	public List<SymbolString> generateStringsBrute() {
		clear();
		while (myStringsInLanguage.size() < myNumberToGenerate) {
			makeNextReplacement();
		}

		ArrayList<SymbolString> stringsList = new ArrayList<SymbolString>(
				myStringsInLanguage);
		Collections.sort(stringsList, new StringComparator());
		stringsList.add(new SymbolString(new Symbol("...")));
		return stringsList;
	}
	
	public List<SymbolString> generateStringsLength(){
		clear();
		while (myStringsInLanguage.size() < myNumberToGenerate) {
			checkStrings();
		}

		ArrayList<SymbolString> stringsList = new ArrayList<SymbolString>(
				myStringsInLanguage);
		Collections.sort(stringsList, new StringComparator());
		stringsList.add(new SymbolString(new Symbol("...")));
		return stringsList;
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
						if(myStringsInLanguage.size()>= myNumberToGenerate) break loop;
							Derivation tempDerivation = d.copy();
							tempDerivation.addStep(p, result.indexOf(LHS, i));
							temp.add(tempDerivation);
							if(tempDerivation.createResult().getSymbolsOfClass(Variable.class).size()==0){
								myStringsInLanguage.add(tempDerivation.createResult());
							}
						}
					}
				}
			}
		myDerivationsQueue.addAll(temp);
		return true;
	}

	private class StringComparator implements Comparator<SymbolString> {

		@Override
		public int compare(SymbolString s1, SymbolString s2) {
			if (s1.size() != s2.size())
				return s1.size() - s2.size();
			return s1.toString().compareTo(s2.toString());
		}

	}

	private void checkStrings() {
		
			if (currentStringLength == 0) {
			} else if (currentStringLength == 1) {
				for (Symbol terminal : myGrammar.getTerminals()) {
					myPossibleStrings.add(new SymbolString(terminal));
				}
			} else {
				getNextLengthStrings();
			}
			BruteParser parser = new RestrictedBruteParser(myGrammar);
			for (SymbolString string : myPossibleStrings) {
				if (parser.quickParse(string) && myStringsInLanguage.size() < myNumberToGenerate) {
					myStringsInLanguage.add(string);
				}
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
}
