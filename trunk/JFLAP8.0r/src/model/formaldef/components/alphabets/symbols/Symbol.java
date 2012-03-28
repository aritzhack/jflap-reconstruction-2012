package model.formaldef.components.alphabets.symbols;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Symbol implements Comparable<Symbol>, Cloneable{

	private String myString;
	
	public Symbol(String s) { 
		myString = s;
	}
	
	public String getString(){
		return myString;
	}
	
	public void setString(String s){
		myString = s;
	}


	@Override
	public int hashCode() {
		return myString.hashCode();
	}


	public boolean containsCharacters(char ... chars) {
		return SymbolHelper.containsCharacters(this, chars);
	}
	

	public int length() {
		return myString.length();
	}

	
	public String getName(){
		return this.getClass().getSimpleName();
	}
	
	@Override
	public boolean equals(Object o){
		return this.getString().equals(((Symbol) o).getString());
	}
	
	
	@Override
	public int compareTo(Symbol o) {
		return this.getString().compareTo(o.getString());
	}


	public String toString(){
		return this.getString();
	}

	@Override
	public Symbol clone() {
		
		try {
			Symbol s = (Symbol) this.getClass().getConstructors()[0].newInstance(this.getString());
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Problem cloning " + this.toString());
		}
		
	}
	
	
}

