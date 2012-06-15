package model.automata.transducers;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.TreeSet;

import util.UtilFunctions;

import model.automata.AutomatonFunction;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public abstract class OutputFunction<T extends OutputFunction<T>> extends AutomatonFunction<T> {

	private State myState;
	private SymbolString myOutput;

	public OutputFunction(State s, SymbolString output) {
		myState = s;
		myOutput = output;
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
	public abstract boolean matches(FSATransition trans);


	@Override
	public boolean setTo(OutputFunction other) {
		this.setState(other.getState());
		return this.setOutput(other.getOutput());
	}

	public void setState(State state) {
		myState = state;
	}

	public boolean setOutput(SymbolString out) {
		return myOutput.setTo(out);
	}

	@Override
	public SymbolString[] getPartsForAlphabet(Alphabet a) {
		if (a instanceof OutputAlphabet)
			return new SymbolString[]{getOutput()};
		return new SymbolString[0];
	}

	@Override
	public int compareTo(OutputFunction o) {
		return UtilFunctions.metaCompare(
				new Comparable[]{this.getState(), this.getOutput()},
				new Comparable[]{o.getState(), o.getOutput()});
	}
}

