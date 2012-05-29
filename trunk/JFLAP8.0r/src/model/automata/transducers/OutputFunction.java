package model.automata.transducers;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import model.automata.State;
import model.automata.acceptors.fsa.FSTransition;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public abstract class OutputFunction implements LanguageFunction {

	private State myState;
	private SymbolString myOutput;

	public OutputFunction(State s, SymbolString output) {
		myState = s;
		myOutput = output;
	}
	
	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		return myOutput.getUniqueSymbolsUsed();
	}

	@Override
	public boolean purgeOfSymbol(Symbol s) {
		return myOutput.purgeOfSymbol(s);
	}
	
	public State getState(){
		return myState;
	}
	
	public SymbolString getOutput(){
		return myOutput;
	}

	@Override
	public OutputFunction copy() {
		try {
			return this.getClass().getConstructor(State.class, SymbolString.class).newInstance(myState.copy(),
																								myOutput.copy());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	/**
	 * Checks to see if this output function is associated
	 * with the transition passed in as an argument. This
	 * is simply a helper method.
	 * @param trans
	 * @return
	 */
	public abstract boolean matches(FSTransition trans);
	
}
