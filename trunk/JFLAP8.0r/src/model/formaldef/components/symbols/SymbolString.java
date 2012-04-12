package model.formaldef.components.symbols;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import universe.Universe;
import util.Copyable;

import model.formaldef.FormalDefinition;
import model.formaldef.UsesSymbols;
import model.formaldef.components.alphabets.Alphabet;
import model.util.UtilFunctions;





public class SymbolString extends LinkedList<Symbol> implements Comparable<SymbolString>, UsesSymbols, Copyable {

	public SymbolString(String in, FormalDefinition def){
		super();
		this.addAll(SymbolString.createFromString(in, def));
	}

	public SymbolString() {
		super();
	}

	public SymbolString(Symbol ... symbols) {
		super();
		for (Symbol s: symbols)
			this.add(s);
	}

	public SymbolString(SymbolString subList) {
		super(subList);
	}

	public <T extends Symbol> Set<T> getSymbolsOfClass(Class<T> clazz) {
		Set<T> results = new TreeSet<T>();
		for (Symbol s: this){
			if (clazz.isAssignableFrom(s.getClass()))
				results.add((T) s);
		}
		
		return results;
	}
	
	public SymbolString concat(SymbolString sym) {
		this.addAll(sym);
		return this;
	}

	public SymbolString reverse() {
		SymbolString reverse = new SymbolString();
		for (Symbol s: this)
			reverse.addFirst(s);
		return reverse;
	}
	
	public int indexOfSubSymbolString(SymbolString o) {
		for (int i = 0; i <= this.size()-o.size(); i++){
			Boolean check = true;
			for (int j = 0; j < o.size(); j++){
				check = check && this.get(i+j).equals(o.get(j));
			}
			if (check) return i;
		}
		return -1;
	}

	public boolean startsWith(SymbolString label) {
		return label.isEmpty() || this.indexOfSubSymbolString(label) == 0;
	}

	/**
	 * THIS IS NOT THE SAME AS this.toString().length in the case of an empty string
	 * @return
	 */
	public int stringLength() {
		return this.isEmpty() ? 0 : this.toString().length();
	}

	public boolean endsWith(SymbolString ss) {
		return this.indexOfSubSymbolString(ss) + ss.size() == this.size();
	}

	public boolean endsWith(Symbol s) {
		return this.getLast() == s;
	}

	public SymbolString subList(int i) {
		return (SymbolString) this.subList(i, this.size());
	}

	@Override
	public SymbolString subList(int start, int end){
		return new SymbolString(super.subList(start, end).toArray(new Symbol[0]));
	}

	public SymbolString[] splitOnIndex(int position) {
		return new SymbolString[]{this.subList(0, position), this.subList(position)};
	}

	public String toString(){
		return this.isEmpty() ? Universe.curProfile.getEmptyString() : UtilFunctions.createDelimitedString(this, 
				Universe.curProfile.getSymbolStringDelimiter());
	}

	public boolean equals(Object o){
		if (o instanceof SymbolString)
			return this.compareTo((SymbolString) o) == 0;		
		if (o instanceof Symbol)
			return this.size() == 1 && this.getFirst() == o;
		return false;
	}

	@Override
	public int hashCode() {
		int code = 0;
		for (Symbol s: this)
			code += s.hashCode();
		return code;
	}

	@Override
	public SymbolString clone() {
		return this.copy();
	}
	
	
	@Override
	public SymbolString copy() {
		SymbolString string = new SymbolString();
		for (Symbol s: this)
			string.add(s.copy());
		return string;
	}

	@Override
	public int compareTo(SymbolString o) {
		Iterator<Symbol> me = this.iterator(),
		 		 other = o.iterator();
		while(me.hasNext() && other.hasNext()){
			Symbol sMe = me.next(),
				   sOther = other.next();
			
			if(sMe.compareTo(sOther) != 0)
					return sMe.compareTo(sOther);
		}
		
		if (!me.hasNext() && other.hasNext())
			return -1;
		if (me.hasNext() && !other.hasNext())
			return 1;
		
		return 0;
	}

	@Override
	public int indexOf(Object other){
		if (other instanceof Symbol)
			return super.indexOf(other);
		return indexOfSubSymbolString((SymbolString) other);
		
	}

	@Override
	public boolean purgeOfSymbol(Symbol s) {
		boolean result = false;
		while (this.contains(s)){
			result = this.remove(s) || result;
		}
		return result;
	}

	public Symbol replace(int i, Symbol write) {
		Symbol replaced = null;
		if ((replaced = this.remove(i)) != null){
			this.add(i, write);
		}
		return replaced;
	}

	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		return new TreeSet<Symbol>(this);
	}

	public static SymbolString createFromString(String in,
			FormalDefinition def) {
		return createFromString(in, def.getAlphabets().toArray(new Alphabet[0]));
		
	}
	
	public static SymbolString createFromString(String in,
			Alphabet ... alphs) {
		
		String temp = "";
		SymbolString symbols = new SymbolString();
		if (in == null || in.isEmpty()) return symbols;
		for (int i = 0; i < in.length(); i++){
			temp += in.charAt(i);
			for (Alphabet alph: alphs){
				if (alph.containsSymbolWithString(temp)){
					symbols.add(alph.getByString(temp));
					temp = "";
					break;
				}
			}
		}
		return symbols;
	}
	
	public static boolean checkAndSpawnError(String error, String in, Alphabet ... alphs){
		if (!SymbolString.canBeParsed(in, alphs)){
			JOptionPane.showMessageDialog(null, 
					error,
					"Bad Input",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public static SymbolString concat(SymbolString ... strings) {
		SymbolString concat = new SymbolString();
		for (SymbolString ss: strings)
			concat.concat(ss);
		return concat;
	}

	public static boolean canBeParsed(String input, FormalDefinition def) {
		return createFromString(input, def).stringLength() == input.length();
	}
	
	public static boolean canBeParsed(String input, Alphabet ... alphs) {
		return createFromString(input, alphs).stringLength() == input.length();
	}


}
