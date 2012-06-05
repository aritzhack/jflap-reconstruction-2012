package model.grammar.parsing;

import java.lang.Character.Subset;
import java.util.Arrays;
import java.util.LinkedList;

import model.formaldef.components.symbols.SymbolString;
import model.grammar.Production;
import model.util.UtilFunctions;

public class Derivation {

	private LinkedList<Production> myProductions;
	private LinkedList<Integer> mySubstitutions;
	
	public Derivation(Production start) {
		myProductions = new LinkedList<Production>();
		mySubstitutions = new LinkedList<Integer>();
		myProductions.add(start);
	}
	
	public void addAll(Production[] productions, Integer[] subs) {
		if (productions.length != subs.length)
			throw new ParserException("The number of productions and " +
					"substituations in the derivation must be equal.");
		for (int i = 0; i< productions.length; i++){
			addStep(productions[i], subs[i]);
		}
	}

	public boolean addStep(Production p, int subIndex) {
		return myProductions.add(p) && mySubstitutions.add(subIndex);
	}
	
	public SymbolString createResult(){
		SymbolString result = new SymbolString();
		if (myProductions.isEmpty())
		result.addAll(myProductions.getFirst().getRHS());
		for (int i = 1; i < myProductions.size(); i++){
			SymbolString sub = myProductions.get(i).getRHS();
			result.replace(mySubstitutions.get(i-1), sub);
		}
		return result;
	}
	
	@Override
	public String toString() {
		return myProductions.toString();
	}
	
}
