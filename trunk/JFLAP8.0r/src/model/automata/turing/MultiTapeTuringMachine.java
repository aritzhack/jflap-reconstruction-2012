package model.automata.turing;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;

public class MultiTapeTuringMachine extends TuringMachine<MultiTapeTMTransition>{

	private int myNumTapes;
	
	public MultiTapeTuringMachine(StateSet states, TapeAlphabet tapeAlph,
			BlankSymbol blank, InputAlphabet inputAlph,
			TransitionSet<MultiTapeTMTransition> functions, StartState start,
			FinalStateSet finalStates, int numTapes) {
		super(states, tapeAlph, blank, inputAlph, functions, start, finalStates);
		myNumTapes = numTapes;
	}
	
	@Override
	public String getDescriptionName() {
		return "Turing Machine (TM)";
	}

	@Override
	public String getDescription() {
		return "A basic multi-tape turing machine";
	}
	

	public int getNumTapes() {
		return myNumTapes;
	}
	
	@Override
	public MultiTapeTuringMachine copy() {
		return new MultiTapeTuringMachine(this.getStates().copy(),
				this.getTapeAlphabet().copy(),
				new BlankSymbol(),
				this.getInputAlphabet().copy(), 
				this.getTransitions().copy(), 
				new StartState(this.getStartState().copy()), 
				this.getFinalStateSet().copy(),
				myNumTapes);
	}
	
	@Override
	public MultiTapeTuringMachine alphabetAloneCopy() {
		return new MultiTapeTuringMachine(new StateSet(),
									this.getTapeAlphabet(), 
									new BlankSymbol(),
									this.getInputAlphabet(), 
									new TransitionSet<MultiTapeTMTransition>(), 
									new StartState(), 
									new FinalStateSet(),
									myNumTapes);
	}

}
