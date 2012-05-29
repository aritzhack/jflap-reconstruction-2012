package model.automata.simulate.configurations;

import java.util.LinkedList;

import universe.preferences.JFLAPPreferences;

import model.automata.State;
import model.automata.simulate.Configuration;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineTransition;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class TMConfiguration extends Configuration<TuringMachine, TuringMachineTransition> {


	public TMConfiguration(TuringMachine tm, State s, int[] pos,
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
	protected int getNextPrimaryPosition(TuringMachineTransition label) {
		//primary is not used
		return 0;
	}

	@Override
	protected Configuration<TuringMachine, TuringMachineTransition> createConfig(TuringMachine tm,
			State s, int ppos, SymbolString primary, int[] positions,
			SymbolString[] updatedClones) throws Exception {
		return new TMConfiguration(tm, s, positions, updatedClones);
	}

	
	
	@Override
	public LinkedList<Configuration<TuringMachine, TuringMachineTransition>> getNextConfigurations() {
		// TODO Auto-generated method stub
		return super.getNextConfigurations();
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

	@Override
	protected boolean canMoveAlongTransition(TuringMachineTransition trans) {
		for (int i = 0; i < super.getNumOfSecondary(); i++){
			if (!isValidMoveForTape(i, trans.getReadForTape(i)))
				return false;
		}
		return true;
	}

	private boolean isValidMoveForTape(int i, SymbolString readString) {
		int position = this.getPositionForIndex(i);
		SymbolString tape = this.getStringForIndex(i);
		Symbol read;
		if (readString.isEmpty())
			read = this.getAutomaton().getBlankSymbol();
		else 
			read = readString.getFirst();
		return tape.get(position).equals(read);
	}

	@Override
	protected int getNextSecondaryPosition(int i,
			TuringMachineTransition trans) {
		int move = trans.getMoveForTape(i);
		return this.getPositionForIndex(i) + move;
	}

	@Override
	protected SymbolString[] assembleUpdatedStrings(SymbolString[] clones,
			TuringMachineTransition trans) {
		for (int i = 0; i < this.getNumOfSecondary(); i++){
			SymbolString writeString = trans.getWriteForTape(i);
			Symbol write;
			if (writeString.isEmpty())
				write = this.getAutomaton().getBlankSymbol();
			else
				write = writeString.getFirst();
			clones[i].replace(this.getPositionForIndex(i), write);
		}
		return clones;
	}


	

}
