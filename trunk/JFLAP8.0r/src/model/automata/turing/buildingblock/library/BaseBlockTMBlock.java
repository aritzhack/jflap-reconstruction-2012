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
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.change.events.AdvancedChangeEvent;

public abstract class BaseBlockTMBlock extends Block implements ChangeListener{

	public BaseBlockTMBlock(TapeAlphabet alph, BlankSymbol blank, String name, int id) {
		super(createTuringMachine(blank, alph), name, id);
		alph.addListener(this);
	}
	
	private static BlockTuringMachine createTuringMachine(BlankSymbol blank, TapeAlphabet alph) {
		
		BlockTuringMachine tm = new BlockTuringMachine(new BlockSet(), 
																alph.copy(),
																blank, 
																new InputAlphabet(), 
																new TransitionSet<BlockTransition>(),
																new StartState(), 
																new FinalStateSet());
		return tm;
	}
	
	@Override
	public BlockTuringMachine getTuringMachine() {
		return (BlockTuringMachine) super.getTuringMachine();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(!(e instanceof AdvancedChangeEvent))
		return;
		AdvancedChangeEvent event = (AdvancedChangeEvent) e;
		if(event.comesFrom(TapeAlphabet.class)){
			updateTuringMachine((TapeAlphabet) event.getSource());
		}
	}
	
	public void addStartAndFinalStates(){
		TuringMachine tm = getTuringMachine();
		StateSet states = tm.getStates();
		
		State start = states.createAndAddState();
		tm.setStartState(start);
		
		State finalState = states.createAndAddState();
		tm.getFinalStateSet().add(finalState);
	}
	
	public abstract void updateTuringMachine(TapeAlphabet tape);

}
