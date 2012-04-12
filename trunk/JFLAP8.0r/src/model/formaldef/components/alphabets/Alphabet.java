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

import model.automata.InputAlphabet;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.rules.AlphabetRule;
import model.formaldef.rules.GroupingRule;
import model.formaldef.rules.applied.BaseRule;



public abstract class Alphabet extends SetComponent<Symbol>{
	
	

	private Set<AlphabetRule> myRules;
	
	public Alphabet(){
		myRules = new TreeSet<AlphabetRule>();
		this.addRules(new BaseRule());
	}
	
	

	@Override
	public boolean add(Symbol s) {
		this.checkRules(AlphabetActionType.ADD, s);
		return super.add(s);
	}

	@Override
	public boolean remove(Object s) {
		this.checkRules(AlphabetActionType.REMOVE, (Symbol) s);
		return super.remove(s);
	}
	
	@Override
	public boolean removeAll(Collection<? extends Object> symbols) {
		boolean removed = false;
		for (Object s: symbols){
			removed = this.remove((Symbol) s) || removed;
		}
		return removed;
	}

	public boolean equals(Object o){
		if (!super.equals(o))
			return false;
		Alphabet other = (Alphabet) o;
		return Arrays.equals(super.toArray(), other.toArray());
		
	}
	
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(!this.isEmpty(), "The " + this.toString() + 
				" is incomplete because it is empty.");
	}

	
	public boolean addAll(Symbol... symbols) {
		return this.addAll(Arrays.asList(symbols));
	}

	
	public void modify(Symbol oldSymbol, Symbol newSymbol) {
		this.checkRules(AlphabetActionType.MODIFY, oldSymbol, newSymbol);
		this.getByString(oldSymbol.toString()).setString(newSymbol.toString());
		this.distributeChange(ALPH_SYMBOL_MODIFY, oldSymbol, newSymbol);
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
	public Alphabet clone() {
		
		try {
			Alphabet alph = this.getClass().newInstance();
			for (Symbol s: this)
				alph.add((Symbol) s.copy());
			for (AlphabetRule rule: this.getRules())
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

	public <T extends GroupingRule> GroupingRule getRuleOfClass(Class<T> clz) {
		for (AlphabetRule rule : this.getRules()){
			if (clz.isAssignableFrom(rule.getClass()))
				return clz.cast(rule);
		}
		return null;
	}
	
	public abstract String getSymbolName();

	public AlphabetRule[] getRules() {
		return myRules.toArray(new AlphabetRule[0]);
	}

	@Override
	public int hashCode() {
		int hash = 1;
		for (Symbol s: this){
			hash *= s.hashCode();
		}
		return (1+hash);
	}

	public boolean removeRule(AlphabetRule rule){
		return myRules.remove(rule);
	}

	public boolean addRules(AlphabetRule ... rules){
		return myRules.addAll(Arrays.asList(rules));
	}

	private void checkRules(AlphabetActionType type, Symbol ... symbols) throws AlphabetException{
			for (AlphabetRule rule : myRules){
				BooleanWrapper bw = new BooleanWrapper(true);
				switch (type){
				case ADD: 
					bw = rule.canAdd(this, symbols[0]); break;
				case REMOVE:
					bw = rule.canRemove(this, symbols[0]); break;
				case MODIFY:
					bw = rule.canModify(this, symbols[0], symbols[1]); break;
				}
				
				if (bw.isFalse())
					throw new AlphabetException(bw.getMessage());
				
			}
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
