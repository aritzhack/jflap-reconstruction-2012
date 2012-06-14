package model.formaldef.components.alphabets;


import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;

import model.change.rules.Rule;
import model.change.rules.applied.BaseRule;
import model.formaldef.components.SetComponent;
import model.formaldef.components.symbols.Symbol;



public abstract class Alphabet extends SetComponent<Symbol>{
	
	
	
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(!this.isEmpty(), "The " + this.toString() + 
				" is incomplete because it is empty.");
	}

	public boolean containsSymbolWithString(String... strings) {
		for	(String s: strings){
			if (!this.contains(new Symbol(s))) 
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
	
	@Override
	public Alphabet copy() {
		
		try {
			Alphabet alph = this.getClass().newInstance();
			for (Symbol s: this)
				alph.add((Symbol) s.copy());
			for (Rule rule: this.getRules())
				alph.addRules(rule);
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
		for (Symbol s : this){
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

	public abstract String getSymbolName();


	@Override
	public int hashCode() {
		int hash = 1;
		for (Symbol s: this){
			hash *= s.hashCode();
		}
		return (1+hash);
	}


	public static boolean addCopiedSymbols(Alphabet alph,
			Symbol[] toAdd) {
		boolean converted = true;
		for (Symbol s: toAdd){
			converted = converted && alph.add(new Symbol(s.getString()));
		}
		return converted;
	}


}
