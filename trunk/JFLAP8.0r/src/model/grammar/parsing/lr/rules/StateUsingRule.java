package model.grammar.parsing.lr.rules;

import model.automata.State;
import model.automata.acceptors.fsa.FSTransition;
import model.formaldef.components.symbols.Terminal;
import model.grammar.parsing.lr.SLR1DFAState;

public abstract class StateUsingRule extends SLR1rule {

	private State myToState;

	public StateUsingRule(State to) {
		myToState = to;
	}

	public State getToState() {
		return myToState;
	}
	
}
