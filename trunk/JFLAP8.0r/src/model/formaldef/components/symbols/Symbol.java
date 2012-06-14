package model.formaldef.components.symbols;


import debug.JFLAPDebug;
import model.change.events.SetToEvent;
import model.formaldef.components.SetSubComponent;
import model.regex.EmptySub;


public class Symbol extends SetSubComponent<Symbol>{

	private String myString;
	
	public Symbol(String s) { 
		myString = s;
	}
	
	public Symbol(Character c) {
		this("" + c);
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
		return "This is a symbol!";
	}

	@Override
	public boolean setTo(Symbol other) {
		return applyChange(new SymbolSetToEvent(other));
	}
	
	private class SymbolSetToEvent extends SetToEvent<Symbol, Symbol>{

		public SymbolSetToEvent(Symbol to) {
			super(Symbol.this, Symbol.this.copy(), to);
		}

		@Override
		public boolean undo() {
			return setTo(getFrom());
		}

		@Override
		public boolean redo() {
			return setTo(getTo());
		}

		@Override
		public String getName() {
			return "Set " + getDescriptionName();
		}

		@Override
		public boolean applyChange() {
			Symbol.this.myString = this.getTo().myString;
			return true;
		}
		
	}
}

