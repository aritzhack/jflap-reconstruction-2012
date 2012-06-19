package model.automata.simulate.configurations.tm;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.simulate.Configuration;
import model.automata.turing.TuringMachine;
import model.automata.turing.MultiTapeTMTransition;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public abstract class TMConfiguration<S extends TuringMachine<T>, T extends Transition<T>> 
														extends Configuration<S,T> {

	public TMConfiguration(S tm, State s, int[] pos,
			SymbolString ... tapes) {
		super(tm, s, 0, null, pos, tapes);
	}

	@Override
	public String getName() {
		return "TM Configuration";
	}

	@Override
	protected String getPrimaryPresentationName() {
		//primary is not used
		return null;
	}

	@Override
	protected int getNextPrimaryPosition(T label) {
		//primary is not used
		return 0;
	}

	@Override
	protected boolean shouldFindValidTransitions() {
		//Turing machine is never "done". if a valid transitions
		//from a state exists, it should be moved on.
		return true;
	}

	@Override
	protected boolean isDone() {
		//a Turing machine is done iff it does not have a next state
		return !this.hasNextState();
	}

	@Override
	protected String getStringPresentationName(int i) {
		return "Tape " + i;
	}
	
	public Symbol getReadForTape(int i){
		return getStringForIndex(i).get(getPositionForIndex(i));
	}
}
