package model.grammar.parsing;

import java.lang.Character.Subset;
import java.util.Arrays;
import java.util.LinkedList;

import util.Copyable;

import debug.JFLAPDebug;

import model.formaldef.components.symbols.SymbolString;
import model.grammar.Production;
import model.util.UtilFunctions;

public class Derivation implements Copyable{

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
		return createResult(this.getLength());
	}
	
	/**
	 * Returns the number of productions in this derivation.
	 * @return
	 */
	public int getLength() {
		return myProductions.size();
	}

	public SymbolString createResult(int n){
		SymbolString result = new SymbolString();
		if (getLength() < n){
			throw new ParserException("This derivation does not have " + n +
					" steps."	);
		}
		
		if (n == 0) return result;
		
		result.addAll(myProductions.getFirst().getRHS());
		
		for (int i = 1; i < n; i++){
			SymbolString sub = myProductions.get(i).getRHS();
			int start = mySubstitutions.get(i-1);
			int end = start + myProductions.get(i).getLHS().size();
			result.replace(start,end, sub);
		}
		return result;
	}
	
	public SymbolString[] getResultArray(){
		SymbolString[] steps = new SymbolString[this.getLength()];
		for (int i = 1; i < steps.length+1; i++){
			steps[i-1] = createResult(i);
		}
		return steps;
	}
	
	@Override
	public String toString() {
		return myProductions.toString();
	}

	public Production getProduction(int i) {
		return myProductions.get(i);
	}

	@Override
	public Derivation copy() {
		Derivation copy = new Derivation(myProductions.get(0));
		for (int i = 1; i< this.getLength(); i++){
			copy.addStep(myProductions.get(i), mySubstitutions.get(i-1));
		}
		return copy;
	}
	
}
