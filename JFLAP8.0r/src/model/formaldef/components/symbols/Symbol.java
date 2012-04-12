package model.formaldef.components.symbols;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import util.Copyable;

public class Symbol implements Comparable<Symbol>, Copyable{

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
	public Symbol copy() {
		
		try {
			Symbol s = (Symbol) this.getClass().getConstructor(String.class).newInstance(this.getString());
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Problem cloning " + this.toString());
		}
		
	}
	
	
}

