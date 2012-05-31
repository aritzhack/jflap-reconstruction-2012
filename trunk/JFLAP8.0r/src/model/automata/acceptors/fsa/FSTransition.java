package model.automata.acceptors.fsa;

import java.util.Set;

import model.automata.State;
import model.automata.Transition;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.regex.EmptySub;

public class FSTransition extends Transition {

	
	public FSTransition(State from, State to, SymbolString input) {
		super(from, to, input);
	}

	public FSTransition(State from, State to) {
		this(from, to, new SymbolString());
	}

	public FSTransition(State from, State to, Symbol s) {
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
	public FSTransition copy() {
		return new FSTransition(this.getFromState().copy(), 
									this.getToState().copy(), 
									this.getInput().copy());
	}

	@Override
	public String getLabelText() {
		return this.getInput().toString();
	}

	
}
