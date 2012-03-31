package model.automata.transducers;

import java.util.Set;

import model.automata.State;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;
import model.formaldef.components.functionset.function.LanguageFunction;

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

}
