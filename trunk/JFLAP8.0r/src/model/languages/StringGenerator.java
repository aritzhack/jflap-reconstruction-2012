package model.languages;

/**
 * Utility class for generating a set of strings
 * in a language given a restricted grammar 
 * for the language
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import model.algorithms.conversion.autotogram.FSAtoRegGrammarConversion;
import model.algorithms.conversion.autotogram.PDAtoCFGConverter;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.parsing.Derivation;

public class StringGenerator {

	public static final int DEFAULT_NUMBER_TO_GENERATE = 10;
	
	private Grammar myGrammar;

	private Queue<Derivation> myDerivationsQueue;
	private Set<SymbolString> myStringsInLanguage;
	
	private int myNumberToGenerate;
	

	public StringGenerator (Grammar g) {
		this(g, DEFAULT_NUMBER_TO_GENERATE);
	}
	
	public StringGenerator (FiniteStateAcceptor fsa) {
		this(new FSAtoRegGrammarConversion(fsa).getConvertedGrammar());
	}
	
	
	public StringGenerator (PushdownAutomaton pda) {
		this(new PDAtoCFGConverter(pda).getConvertedGrammar());
	}
	
	
	public StringGenerator (Grammar g, int numberToGenerate) {
		myGrammar = g;
		myNumberToGenerate = numberToGenerate;

		myDerivationsQueue = new LinkedList<Derivation>();
		myStringsInLanguage = new HashSet<SymbolString>();

		SymbolString start = new SymbolString(myGrammar.getStartVariable());
		Derivation d = new Derivation(new Production(new SymbolString(), start));
		myDerivationsQueue.add(d);
		
	}

	
	public void generateStrings () {
		while (myStringsInLanguage.size() < myNumberToGenerate) {
			makeNextReplacement();
			if (myDerivationsQueue.isEmpty()) 
				break;
		}
		
		ArrayList<SymbolString> stringsList = new ArrayList<SymbolString>(myStringsInLanguage);
		Collections.sort(stringsList, new StringComparator());
		System.out.println("Strings: " + stringsList);
		
	}
	
	private boolean makeNextReplacement() {
		
		ArrayList<Derivation> temp = new ArrayList<Derivation>();

		loop: while (!myDerivationsQueue.isEmpty()) {
			Derivation d = myDerivationsQueue.poll();
			SymbolString result = d.createResult();
			
			for (int i = 0; i < result.size(); i++) {
				for (int j = i; j < result.size(); j++) {
					SymbolString LHS = result.subList(i, j + 1);
					
					for (Production p : myGrammar.getProductionSet()
							.getProductionsWithLHS(LHS)) {
						
						Derivation tempDerivation = d.copy();
						tempDerivation.addStep(p, result.indexOf(LHS, i));
						SymbolString str = tempDerivation.createResult();
					
						if (str.getSymbolsOfClass(Variable.class).size() == 0) {
						
							myStringsInLanguage.add(str);
						}
						
						if (myStringsInLanguage.size() >= myNumberToGenerate)
							break loop;
						
						temp.add(tempDerivation);
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

}