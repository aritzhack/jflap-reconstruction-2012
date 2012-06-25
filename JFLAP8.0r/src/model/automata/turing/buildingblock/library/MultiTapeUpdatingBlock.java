package model.automata.turing.buildingblock.library;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
import model.automata.turing.buildingblock.UpdatingBlock;
import model.change.events.AdvancedChangeEvent;

public abstract class MultiTapeUpdatingBlock extends UpdatingBlock implements ChangeListener{

	

	public MultiTapeUpdatingBlock(TapeAlphabet parentAlph, String name, 
										int id, Object...args) {
		super(parentAlph, name, id, args);
	}

	public static void addStartAndFinalStates(TuringMachine tm){
		StateSet states = tm.getStates();
		
		State start = states.createAndAddState();
		tm.setStartState(start);
		
		State finalState = states.createAndAddState();
		tm.getFinalStateSet().add(finalState);
	}
	

}
