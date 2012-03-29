package model.formaldef.components.alphabets;


import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.components.alphabets.symbols.Symbol;



public abstract class Alphabet extends TreeSet<Symbol> implements FormalDefinitionComponent{
	
	
	public Alphabet(){
		myGrouping = new GroupingPair();
	}
	
	public String toString() {
		return this.getDescriptionName() + ": " + super.toString();
	}

	@Override
	public boolean equals(Object o){
		if (!super.equals(o))
			return false;
		Alphabet other = (Alphabet) o;
		Set<Symbol> a1 = this.clone(),
				a2 = other.clone();
		a1.removeAll(other);
		a2.removeAll(this);
		return a1.isEmpty() && a2.isEmpty();
		
	}
	
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(!this.isEmpty(), "The " + this.toString() + 
				" is incomplete because it is empty.");
	}

	
	public boolean addAll(Symbol... symbols) {
		return this.addAll(Arrays.asList(symbols));
	}

	
	public void modify(Symbol oldSymbol, Symbol newSymbol) {
		this.getByString(oldSymbol.toString()).setString(newSymbol.toString());
	}

	public boolean containsSymbolWithString(String... strings) {
		for	(String s: strings){
			if (!this.contains(new Symbol(s))) 
				return false; 
		}
		return true;
	}

	
	public boolean contains(Symbol... symbols) {
		for	(Symbol s: symbols){
			if (!this.contains(s))
					return false; 
		}
		return true;
	}


	public Symbol getByString(String sym) {
		for (Symbol s: this){
			if (sym.equals(s.getString()))
				return s;
		}
		return null;
	}

	public Set<Character> getUniqueCharacters() {
		Set<Character> chars = new TreeSet<Character>();
		for (Symbol s: this){
			for (char c : s.getString().toCharArray()){
				chars.add(c);
			}
		}
		return chars;
	}

	
	public Symbol getFirstSymbolContaining(char ... chars) {
		for (Symbol s: this){
			if (s.containsCharacters(chars))
				return s;
		}
		return null;
	}
	
	public Alphabet clone() {
		
		try {
			Alphabet alph = this.getClass().newInstance();
			for (Symbol s: this)
				alph.add((Symbol) s.clone());
			return alph;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AlphabetException("Error cloning the alphabet");
		}
		
	}
	
	/**
	 * Returns the "index" of the item in this Alphabet.
	 * 
	 * @param variable
	 *            the variable to find the row for
	 * @return the index of the symbol
	 * 				-1 if the symbol is not in the ALphabet
	 */
	public int getIndex(Symbol sym) {
		int index = this.size()-1;
		for (Symbol s : this.descendingSet()){
			if (s.equals(sym))
				break;
			index--;
		}
		return index;	
	}
	
	public String[] getSymbolStringArray() {
		String[] strings = new String[this.size()];
		Iterator<Symbol> iter = this.iterator();
		for (int i = 0; i < strings.length; i++){
			strings[i] = iter.next().getString();
		}
		return strings;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		for (Symbol s: this){
			hash *= s.hashCode();
		}
		return (1+hash);
	}

	public abstract String getSymbolName();
	
	

	
	//**************************************************//
	//***************    GROUPING     ******************//
	//**************************************************//
	
	private GroupingPair myGrouping;


	public void setGrouping(GroupingPair gp) {
		myGrouping = gp;
	}


	public GroupingPair getGrouping() {
		return myGrouping;
	}


	public boolean usingGrouping() {
		return myGrouping.isUsable();
	}

}
