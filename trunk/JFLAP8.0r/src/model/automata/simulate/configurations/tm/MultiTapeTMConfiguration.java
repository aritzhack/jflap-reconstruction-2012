package model.automata.simulate.configurations.tm;

import java.util.LinkedList;

import debug.JFLAPDebug;

import model.automata.State;
import model.automata.simulate.Configuration;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.MultiTapeTMTransition;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class MultiTapeTMConfiguration extends TMConfiguration<MultiTapeTuringMachine, MultiTapeTMTransition> {


	public MultiTapeTMConfiguration(MultiTapeTuringMachine tm, State s, int[] pos,
			SymbolString ... tapes) {
		super(tm, s, pos, tapes);
	}


	@Override
	protected Configuration<MultiTapeTuringMachine, MultiTapeTMTransition> createConfig(MultiTapeTuringMachine tm,
			State s, int ppos, SymbolString primary, int[] positions,
			SymbolString[] updatedClones) throws Exception {
		return new MultiTapeTMConfiguration(tm, s, positions, updatedClones);
	}

	@Override
	protected boolean canMoveAlongTransition(MultiTapeTMTransition trans) {
		for (int i = 0; i < super.getNumOfSecondary(); i++){
			if (!getReadForTape(i).equals(trans.getRead(i)))
				return false;
		}
		return true;
	}

	@Override
	protected int getNextSecondaryPosition(int i,
			MultiTapeTMTransition trans) {
		TuringMachineMove move = trans.getMove(i);
		return this.getPositionForIndex(i) + move.int_move;
	}

	@Override
	protected SymbolString[] assembleUpdatedStrings(SymbolString[] clones,
			MultiTapeTMTransition trans) {
		for (int i = 0; i < this.getNumOfSecondary(); i++){
			Symbol write = trans.getWrite(i);
			clones[i] = clones[i].replace(this.getPositionForIndex(i), write);
		}
		return clones;
	}


	

}
