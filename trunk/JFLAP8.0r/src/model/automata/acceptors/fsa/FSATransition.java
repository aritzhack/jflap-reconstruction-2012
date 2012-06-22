package model.automata.acceptors.fsa;

import java.util.List;
import java.util.Set;

import util.JFLAPConstants;

import model.automata.AutomatonException;
import model.automata.State;
import model.automata.SingleInputTransition;
import model.formaldef.components.alphabets.Alphabet;
import model.regex.EmptySub;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class FSATransition extends SingleInputTransition<FSATransition> {

	
	public FSATransition(State from, State to, SymbolString input) {
		super(from, to, input);
	}

	
	public FSATransition(State from, State to) {
		this(from, to, new SymbolString());
	}

	public FSATransition(State from, State to, Symbol s) {
		this(from, to, new SymbolString(s));
	}

	@Override
	public String getDescriptionName() {
		return "Finite State Transition";
	}

	@Override
	public String getDescription() {
		return "This is a finite state transition, used for all Finite State Machines." +
				" This includes Finite State Acceptors (FSA), Moore Machines, and Mealy Machines.";
	}

	@Override
	public FSATransition copy() {
		return new FSATransition(this.getFromState().copy(), 
									this.getToState().copy(), 
									new SymbolString(getInput()));
	}

	
}
