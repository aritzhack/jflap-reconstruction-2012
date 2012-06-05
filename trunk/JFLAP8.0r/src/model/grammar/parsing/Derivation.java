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
		if (myProductions.size() <= n){
			throw new ParserException("This derivation does not have " + n +
					"steps."	);
		}
		
		if (n != 0)
			result.addAll(myProductions.getFirst().getRHS());
		for (int i = 1; i <= n; i++){
			SymbolString sub = myProductions.get(i).getRHS();
			result.replace(mySubstitutions.get(i-1), sub);
		}
		return result;
	}
	
	public SymbolString[] getResultArray(){
		SymbolString[] steps = new SymbolString[this.getLength()-1];
		for (int i = 1; i <= steps.length; i++){
			steps[i] = createResult(i);
		}
		return steps;
	}
	
	@Override
	public String toString() {
		return myProductions.toString();
	}
	
}
