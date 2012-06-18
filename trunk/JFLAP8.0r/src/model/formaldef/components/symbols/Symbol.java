package model.formaldef.components.symbols;


import java.lang.Character.Subset;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;

import debug.JFLAPDebug;

import model.automata.turing.BlankSymbol;
import model.change.events.SetToEvent;
import model.formaldef.components.SetSubComponent;
import model.regex.EmptySub;

import util.Copyable;

public class Symbol extends SetSubComponent<Symbol>{

	private String myString;
	
	public Symbol(String s) { 
		myString = s;
	}
	
	/**
	 * Returns the string associated with this symbol. In
	 * speacial cases, this may not be used for the actual
	 * toString representation. Therefore, be aware that 
	 * comparisions/internal usage of the {@link Symbol}
	 * class should use this method.
	 * 
	 * @see EmptySub
	 * 
	 * @return
	 */
	public String getString(){
		return myString;
	}
	
	public void setString(String s){
		this.setTo(new Symbol(s));
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

	@Override
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

	@Override
	public String getDescriptionName() {
		return "Symbol";
	}

	@Override
	public String getDescription() {
		return "A symbol!";
	}
	
	@Override
	protected void applySetTo(Symbol other) {
		myString = other.getString();
	}

}

