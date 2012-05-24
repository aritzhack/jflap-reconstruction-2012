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



import universe.preferences.JFLAPPreferences;
import util.Copyable;

import model.formaldef.FormalDefinition;
import model.formaldef.UsesSymbols;
import model.formaldef.components.alphabets.Alphabet;
import model.regex.OperatorAlphabet;
import model.regex.operators.UnionOperator;
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
		if (o.isEmpty()) return 0;
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
		return this.indexOfSubSymbolString(label) == 0;
	}

	/**
	 * THIS IS NOT THE SAME AS this.toString().length 
	 * in the case of an empty string or delimited string
	 * @return
	 */
	public int stringLength() {
		int length = 0;
		for (Symbol s: this){
			length += s.length();
		}
		return length;
	}

	public boolean endsWith(SymbolString ss) {
		int start = this.size()-ss.size();
		return this.subList(start).equals(ss);
	}

	public boolean endsWith(Symbol s) {
		return this.getLast().equals(s);
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
		return this.isEmpty() ? JFLAPPreferences.getEmptyStringSymbol() : UtilFunctions.createDelimitedString(this, 
				JFLAPPreferences.getSymbolStringDelimiter());
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

		if (in == null ||in.length() == 0 || 
				in == JFLAPPreferences.getEmptyStringSymbol()) 
			return new SymbolString();
		
		in = removeDelimiters(in);
		ArrayList<SymbolString> options = new ArrayList<SymbolString>();
		
		for (int i = in.length(); i > 0; i--){
			SymbolString symbols = new SymbolString();
			String temp = in.substring(0,i);
			for (Alphabet alph: alphs){
				if (alph.containsSymbolWithString(temp)){
					symbols.add(alph.getByString(temp));
					symbols.addAll(createFromString(in.substring(i), alphs));
					break;
				}
			}
			if(symbols.stringLength() == in.length()){
				return symbols;
			}
			else if(!symbols.isEmpty())
				options.add(symbols);
		}
		SymbolString max = new SymbolString();
		for (SymbolString s: options){
			if (max.stringLength() < s.stringLength())
				max = s;
		}
		return max;
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
		return canBeParsed(input, def.getAlphabets().toArray(new Alphabet[0]));
	}
	
	public static boolean canBeParsed(String input, Alphabet ... alphs) {
		return SymbolString.isEmpty(input) || 
				removeDelimiters(createFromString(input, alphs).toString()).equals(removeDelimiters(input));
	}

	private static String removeDelimiters(String input) {
		return input.replaceAll(JFLAPPreferences.getSymbolStringDelimiter(), "");
	}

	public static boolean isEmpty(String input) {
		return input.length() == 0 || input.equals(JFLAPPreferences.getEmptyStringSymbol());
	}

	public boolean replace(int i, Symbol ... replaceWith) {
		return this.replace(i, new SymbolString(replaceWith));

	}

	public boolean replace(int i, SymbolString replaceWith) {
		return this.replace(i,i+1, replaceWith);
	}

	public int indexOf(SymbolString e, int cp) {
		SymbolString temp = this.subList(cp);
		return cp + temp.indexOf(e);
	}

	public boolean replace(int start, int end, SymbolString rhs) {
		
		if (start < 0 || end > this.size() || end < start){
			return false;
		}
		
		for(int i = start; i< end; i++){
			this.remove(start);
		}
		return this.addAll(start, rhs);
	}
	
	public boolean replace(int start, int end, Symbol ... rhs) {
		return this.replace(start, end, new SymbolString(rhs));
	}

	public boolean replaceAll(Symbol toReplace, Symbol ... replaceWith) {
		return this.replaceAll(toReplace, new SymbolString(replaceWith));
	}

	public boolean replaceAll(Symbol toReplace, SymbolString replaceWith) {
		boolean replaced = false;
		for (int i = 0; i < this.size(); i++){
			if(this.replace(toReplace, replaceWith)){
					i += replaceWith.size()-1;
					replaced = true;
			}
		}
		return replaced;
	}

	private boolean replace(Symbol toReplace, SymbolString replaceWith) {
		int index = this.indexOf(toReplace);
		return replace(index, replaceWith);
	}

	public String toNondelimitedString() {
		return UtilFunctions.createDelimitedString(this, "");
	}

	public boolean containsAny(Symbol ... symbols) {
		for (Symbol s: symbols){
			if (this.contains(s))
				return true;
		}
		return false;
	}

	public boolean startsWith(Symbol ... symbols) {
		return this.startsWith(new SymbolString(symbols));
	}


}
