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
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import model.algorithms.conversion.autotogram.FSAtoRegGrammarConversion;
import model.algorithms.conversion.autotogram.PDAtoCFGConverter;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.parsing.Derivation;
import model.grammar.parsing.brute.BruteParser;
import model.grammar.parsing.brute.RestrictedBruteParser;
import model.grammar.parsing.cyk.CYKParser;
import model.grammar.transform.CNFConverter;

public class StringGenerator {

	public static final int DEFAULT_NUMBER_TO_GENERATE = 5;
	
	private Grammar myGrammar;

	private Queue<Derivation> myDerivationsQueue;
	private Set<SymbolString> myStringsInLanguage,myPossibleStrings;
	
	private int myNumberToGenerate, currentStringLength;
	

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

	
	public List<SymbolString> generateStrings () {
		while (myStringsInLanguage.size() < myNumberToGenerate) {
			checkStrings();
		}
		
		ArrayList<SymbolString> stringsList = new ArrayList<SymbolString>(myStringsInLanguage);
		Collections.sort(stringsList, new StringComparator());
		stringsList.add(new SymbolString(new Symbol("...")));
		return stringsList;
		
	}
	
	private class StringComparator implements Comparator<SymbolString> {

		@Override
		public int compare(SymbolString s1, SymbolString s2) {
			if (s1.size() != s2.size())
				return s1.size() - s2.size();
			return s1.toString().compareTo(s2.toString());
		}
		
	}
	
	private void checkStrings(){
		if(currentStringLength == 0){
			myPossibleStrings = new TreeSet<SymbolString>();
		}
		else if(currentStringLength == 1){
			for(Symbol terminal : myGrammar.getTerminals()){
				myPossibleStrings.add(new SymbolString(terminal));
			}
		}else{
			getNextLengthStrings();
		}
		BruteParser parser = new RestrictedBruteParser(myGrammar);
		for(SymbolString string : myPossibleStrings){
			if(parser.quickParse(string)){
				myStringsInLanguage.add(string);
			}
		}
		currentStringLength++;
	}
	
	private void getNextLengthStrings(){
		List<SymbolString> tempList = new ArrayList<SymbolString>();
		tempList.addAll(myPossibleStrings);
			for(SymbolString string : tempList){
				for(Symbol terminal : myGrammar.getTerminals()){
					SymbolString temp = string.copy();
					temp.add(terminal);
					myPossibleStrings.add(temp);
				}
				myPossibleStrings.remove(string);
			}
		}
}
