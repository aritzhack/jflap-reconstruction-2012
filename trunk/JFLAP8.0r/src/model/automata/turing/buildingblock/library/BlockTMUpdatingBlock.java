package model.automata.turing.buildingblock.library;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import debug.JFLAPDebug;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.UpdatingBlock;
import model.change.events.AdvancedChangeEvent;

public abstract class BlockTMUpdatingBlock extends UpdatingBlock{

	
	
	public BlockTMUpdatingBlock(TapeAlphabet parentAlph,
			String name, int id, Object ... args) {
		super(parentAlph, name, id, args);
	}

	@Override
	public BlockTuringMachine getTuringMachine() {
		return (BlockTuringMachine) super.getTuringMachine();
	}
	
	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		BlockSet blocks = getTuringMachine().getStates();
		
		for(State block : blocks){
			if (block instanceof UpdatingBlock)
				((UpdatingBlock) block).updateTuringMachine(tape);
		}
	}
	
}
