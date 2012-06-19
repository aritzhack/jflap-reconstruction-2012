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
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineTransition;
import model.automata.turing.buildingblock.Block;
import model.change.events.AdvancedChangeEvent;

public abstract class BaseBlock extends Block implements ChangeListener{

	public BaseBlock(TapeAlphabet alph, BlankSymbol blank, String name, int id) {
		super(createTuringMachine(blank, alph), name, id);
		alph.addListener(this);
	}
	
	private static TuringMachine createTuringMachine(BlankSymbol blank, TapeAlphabet alph) {
		
		TuringMachine moveMachine = new TuringMachine(new StateSet(), alph.copy(),
				blank, new InputAlphabet(), new TransitionSet<TuringMachineTransition>(),
				new StartState(), new FinalStateSet(), 1);
		return moveMachine;
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
