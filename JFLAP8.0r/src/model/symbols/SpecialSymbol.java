package model.symbols;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import debug.JFLAPDebug;

import errors.BooleanWrapper;
import model.change.events.ModifyEvent;
import model.change.events.SetToEvent;
import model.change.events.SpecialSymbolSetEvent;
import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.StartVariable;
import model.grammar.VariableAlphabet;

public abstract class SpecialSymbol extends FormalDefinitionComponent implements UsesSymbols, ChangeListener {

	private Symbol mySymbol;

	public SpecialSymbol(String s) {
		this (new Symbol(s));
	}

	public SpecialSymbol(Symbol s) {
		if (isPermanent())
			mySymbol = s;
		else
			setSymbol(s);

	}


	public SpecialSymbol(){
		this((Symbol) null);
	}

	public boolean isPermanent(){
		return false;
	}

	public boolean setSymbol(Symbol s) {
		if (isPermanent()) return false;
		if (s == null){
			mySymbol = s;
			return true;
		}
		if (mySymbol == null && s != null){
			mySymbol = s;
			mySymbol.addListener(this);
			distributeChanged();
			return true;
		}
		return mySymbol.setTo(s);

	}

	public Symbol getSymbol() {
		return mySymbol;
	}

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(mySymbol != null,
				"The " + this.getDescriptionName() + " must be set before you can continue");
	}

	@Override
	public String toString() {
		return this.getDescriptionName() + ": " + (getSymbol() == null ? "" : getSymbol().toString());
	}

	@Override
	public SpecialSymbol copy() {
		try {
			return this.getClass().getConstructor(String.class).newInstance(this.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}


	@Override
	public boolean purgeOfSymbols(Alphabet a, Collection<Symbol> s) {
		if (this.equals(s)){
			this.clear();
			return true;
		}
		return false;
	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		Set<Symbol> set = new TreeSet<Symbol>();
		if (getAlphabetClass().isAssignableFrom(a.getClass()) &&
				this.isComplete().isTrue())
			set.add(this.getSymbol());
		return set;
	}

	public abstract Class<? extends Alphabet> getAlphabetClass();

	public void clear() {
		this.setSymbol(null);
	}

	@Override
	public boolean applySymbolMod(String from, String to) {
		if(isPermanent()) return false;
		boolean applies = mySymbol.getString().equals(from) &&
				!mySymbol.getString().equals(to);
		if (applies){
			mySymbol.setString(to);
		}
		distributeChanged();
		return applies;
	}

	public String symbolOnlyString() {
		if (getSymbol() == null) return "";
		return getSymbol().toString();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e instanceof SetToEvent)
			this.distributeChange(new ModifyEvent(this, (SetToEvent) e));
	}
}