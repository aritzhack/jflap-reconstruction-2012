package model.grammar.parsing;

import java.util.Arrays;
import java.util.LinkedList;

import model.grammar.Production;
import model.util.UtilFunctions;

public class Derivation {

	private LinkedList<Production> myProductions;
	private Integer[] mySubstitutions;
	
	public Derivation(Integer[] subs, Production ... productions) {
		this();
		myProductions.addAll(Arrays.asList(productions));
		mySubstitutions = subs;
	}
	
	public Derivation() {
		myProductions = new LinkedList<Production>();
	}

	public boolean addProduction(Production p, int subIndex) {
		mySubstitutions = UtilFunctions.combine(mySubstitutions, subIndex);
		return myProductions.add(p);
	}
	
}
