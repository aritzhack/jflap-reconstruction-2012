package model.formaldef.components.symbols;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;
import model.change.events.SetToEvent;
import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.Settable;

public abstract class SpecialSymbol extends FormalDefinitionComponent implements UsesSymbols, Settable<Symbol> {

	private Symbol mySymbol;

	public SpecialSymbol(String s) {
		this (new Symbol(s));
	}

	public SpecialSymbol(Symbol s) {
		this.setTo(s);
	}


	public SpecialSymbol(){
		this((Symbol) null);
	}

	public boolean setTo(Symbol s) {
		return applyChange(new SetSpecialSymbolEvent(this, s));
	}

	public Symbol toSymbolObject() {
		return mySymbol;
	}

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(mySymbol != null,
				"The " + this.getDescriptionName() + " must be set before you can continue");
	}

	@Override
	public String toString() {
		return this.getDescriptionName() + ": " + (mySymbol == null ? "" : mySymbol.toString());
	}

	@Override
	public SpecialSymbol copy() {
		try {
			return this.getClass().getConstructor(String.class).newInstance(mySymbol.getString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		return new TreeSet<Symbol>(Arrays.asList(new Symbol[]{mySymbol}));
	}

	//	@Override
	//	public boolean purgeOfSymbol(Alphabet a, Symbol s) {
	//		if (this.equals(s)){
	//			this.clear();
	//			return true;
	//		}
	//		return false;
	//	}

	public void clear() {
		this.setTo(null);
	}

	//	@Override
	//	public void applyModification(Symbol from, Symbol to) {
	//		if (this.toSymbolObject().equals(from))
	//			this.setTo(to);
	//	}

	private class SetSpecialSymbolEvent extends SetToEvent<SpecialSymbol, Symbol>{

		public SetSpecialSymbolEvent(SpecialSymbol source,Symbol to) {
			super(source, mySymbol, to);
		}

		@Override
		public String getName() {
			return "Set Special Symbol";
		}

		@Override
		public boolean undo() {
			return setTo(getFrom());
		}

		@Override
		public boolean redo() {
			return true;
		}

		@Override
		public boolean applyChange() {
			mySymbol = this.getTo();
			return true;
		}

	}


}
