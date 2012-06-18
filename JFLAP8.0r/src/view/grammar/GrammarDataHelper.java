package view.grammar;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.JOptionPane;

import util.JFLAPConstants;


import errors.BooleanWrapper;

import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;



public class GrammarDataHelper extends ArrayList<Object[]> implements JFLAPConstants{

	private static final Object[] EMPTY = new Object[]{"",
		ARROW,
	""};
	private LinkedList<BooleanWrapper> myWrappers;
	private ArrayList<Production> myOrderedProductions;
	private Grammar myGrammar;

	public GrammarDataHelper(Grammar model){
		myWrappers = new LinkedList<BooleanWrapper>();
		myGrammar = model;
		myOrderedProductions = 
				new ArrayList<Production>(myGrammar.getProductionSet());
	}




	@Override
	public void add(int index, Object[] input) {
		Production p = this.objectToProduction(input);
		if (isValid(p)) {
			myGrammar.getProductionSet().add(p);
			myOrderedProductions.add(index, p);
		}
	}

	@Override
	public Object[] set(int index, Object[] input) {
		Object[] old = this.get(index);
		if (index >= this.getGrammar().getProductionSet().size())
			this.add(input); 
		else {
			System.out.println(input);
			Production p = this.objectToProduction(input);
			if (isValid(p)){
				Production removed = myOrderedProductions.remove(index);
				myOrderedProductions.add(index,p);
				myGrammar.getProductionSet().remove(removed);
				myGrammar.getProductionSet().add(p);
			}
		}

		return old;
	}

	private boolean isValid(Production p) {
		return !(p == null || p.isEmpty());
	}




	@Override
	public boolean add(Object[] input) {
		int s = this.size()-1;
		this.add(s, input);
		return s < this.size();
	}

	@Override
	public void clear() {
		myGrammar.getProductionSet().clear();
		myOrderedProductions.clear();
	}


	@Override
	public Object[] get(int index) {
		if (index >= myOrderedProductions.size()) return EMPTY;
		return myOrderedProductions.get(index).toArray();
	}

	@Override
	public Object[] remove(int index) {
		if (index >= myOrderedProductions.size()) return EMPTY;
		Production p = myOrderedProductions.remove(index);
		myGrammar.getProductionSet().remove(index);
		return p.toArray();
	}

	@Override
	public int size() {
		return myOrderedProductions.size() + 1;
	}

	@Override
	public Iterator<Object[]> iterator() {
		ArrayList<Object[]> converted = new ArrayList<Object[]>();
		for (Production p: myOrderedProductions)
			converted.add(p.toArray());
		return converted.iterator();
	}


	private Production objectToProduction(Object[] input){
		SymbolString LHS = SymbolString.createFromString((String) input[0], getGrammar()),
				RHS = SymbolString.createFromString((String) input[2], getGrammar());
		if(!SymbolString.canBeParsed((String) input[0], getGrammar())){
			checkAndAddError(new BooleanWrapper(false, 
					"The LHS of this production has a bad character at index " + LHS.toString().length() + "."));
			return null;
		}
		if(!SymbolString.canBeParsed((String) input[2], getGrammar())){
			checkAndAddError(new BooleanWrapper(false, 
					"The RHS of this production has a bad character at index " + RHS.toString().length() + "."));
			return null;
		}
		return new Production(LHS, RHS);
	}


	public Grammar getGrammar(){
		return myGrammar;
	}

	/**
	 * checks if the booleanwrapper is actually an error and adds it to the 
	 * error cache if so. Returns true if the error was added to the cache.
	 * @param error
	 * @return
	 */
	public boolean checkAndAddError(BooleanWrapper error){
		if (error.isError())
			myWrappers.add(error);
		return error.isError();
	}

	public boolean hasErrors(){
		for (BooleanWrapper bw : myWrappers){
			if (bw.isError())
				return true;
		}
		return false;
	}

	public String getAndClearErrors(){
		String message = "The following issues occured in the most recently added production:\n" +
				BooleanWrapper.createErrorLog(myWrappers.toArray(new BooleanWrapper[0]));
		myWrappers.clear();
		return message;
	}




	public void setGrammar(Grammar g) {
		myGrammar = g;
	}




	public void updateProductions() {

	}

}
