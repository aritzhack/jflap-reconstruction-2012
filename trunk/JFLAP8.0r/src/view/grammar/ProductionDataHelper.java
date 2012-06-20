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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import preferences.JFLAPPreferences;


import debug.JFLAPDebug;

import util.JFLAPConstants;


import errors.BooleanWrapper;

import model.change.events.AddEvent;
import model.change.events.RemoveEvent;
import model.change.events.SetToEvent;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.undo.UndoKeeper;



public class ProductionDataHelper extends ArrayList<Object[]> 
									implements JFLAPConstants{

	private static final Object[] EMPTY = new Object[]{"",
		ARROW,
	""};
	private LinkedList<BooleanWrapper> myWrappers;
	private ArrayList<Production> myOrderedProductions;
	private ProductionSet myProductions;
	private UndoKeeper myKeeper;

	public ProductionDataHelper(ProductionSet model, UndoKeeper keeper){
		myKeeper = keeper;
		myWrappers = new LinkedList<BooleanWrapper>();
		myProductions = model;
		myOrderedProductions = 
				new ArrayList<Production>(myProductions);
	}

	@Override
	public void add(int index, Object[] input) {
		Production p = this.objectToProduction(input);
		if(isValid(p)){
			AddEvent<Production> add = 
					new AddEvent<Production>(myProductions, p);
			ProductionTableEvent e = new ProductionTableEvent(add, index);
			if (myKeeper.applyAndCombine(add))
				myOrderedProductions.add(index, p);
		}
	}

	@Override
	public Object[] set(int index, Object[] input) {
		Object[] old = this.get(index);
		if (index >= this.myProductions.size())
			this.add(input); 
		else {
			Production to = this.objectToProduction(input);
			if (isValid(to)){
				Production from = myOrderedProductions.get(index);
				SetToEvent<Production> set = 
						new SetToEvent<Production>(from, from.copy(), to);
				myKeeper.applyAndCombine(set);
			}
			else{
				remove(index);
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
		myProductions.clear();
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
		Production remove = myOrderedProductions.get(index);
		RemoveEvent<Production> event = 
				new RemoveEvent<Production>(myProductions, remove);
		if (myKeeper.applyAndCombine(event)){
			myOrderedProductions.remove(index);
		}
		return remove.toArray();
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

//		if(!SymbolString.canBeParsed((String) input[0], getGrammar())){
//			checkAndAddError(new BooleanWrapper(false, 
//					"The LHS of this production has a bad character at index " + LHS.toString().length() + "."));
//			return null;
//		}
//		if(!SymbolString.canBeParsed((String) input[2], getGrammar())){
//			checkAndAddError(new BooleanWrapper(false, 
//					"The RHS of this production has a bad character at index " + RHS.toString().length() + "."));
//			return null;
//		}
		if (isEmptyString((String) input[0]))
			input[0] = "";
		if (isEmptyString((String) input[2]))
			input[2] = "";
		SymbolString LHS = SymbolString.createForMode((String) input[0]),
				RHS = SymbolString.createForMode((String) input[2]);
		return new Production(LHS, RHS);
	}

	private boolean isEmptyString(String object) {
		return object.equals(JFLAPPreferences.getEmptyStringSymbol());
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

}
