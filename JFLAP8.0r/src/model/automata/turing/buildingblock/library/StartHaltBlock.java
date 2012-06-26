package model.automata.turing.buildingblock.library;

import model.automata.State;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.buildingblock.Block;

public abstract class StartHaltBlock extends Block {


	public StartHaltBlock(String name, int id) {
		super(new MultiTapeTuringMachine(1), name, id);
		
		MultiTapeTuringMachine tm = (MultiTapeTuringMachine) getTuringMachine();
		
		State start = tm.getStates().createAndAddState();
		tm.setStartState(start);
		tm.getFinalStateSet().add(start);
		
	}


}
