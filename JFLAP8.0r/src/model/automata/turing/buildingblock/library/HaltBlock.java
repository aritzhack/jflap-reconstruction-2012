package model.automata.turing.buildingblock.library;

import model.automata.State;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;

public abstract class HaltBlock extends BaseBlock {


	public HaltBlock(TapeAlphabet alph, BlankSymbol blank, String name, int id) {
		super(alph, blank, name, id);
		
		TuringMachine tm = getTuringMachine();
		
		State start = tm.getStates().createAndAddState();
		tm.setStartState(start);
		tm.getFinalStateSet().add(start);
		
		updateTuringMachine(alph);
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		//Do nothing, single state TM used for start and final blocks
	}

}
