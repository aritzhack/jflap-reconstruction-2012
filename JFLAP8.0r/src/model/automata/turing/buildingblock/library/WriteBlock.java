package model.automata.turing.buildingblock.library;

import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.formaldef.components.symbols.Symbol;

public class WriteBlock extends BaseBlock {
	Symbol myWrite;

	public WriteBlock(Symbol write, TapeAlphabet alph, BlankSymbol blank, int id) {
		super(alph, blank, "Write_"+write, id);
		
		myWrite = write;
		
		addStartAndFinalStates();
		
		updateTuringMachine(alph);
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		TransitionSet<MultiTapeTMTransition> transitions = getTuringMachine().getTransitions();
		transitions.clear();

		State start = getTuringMachine().getStartState();
		State finish = getTuringMachine().getFinalStateSet().first();
		
		for(Symbol term : tape){
			transitions.add(new MultiTapeTMTransition(start, finish, term, myWrite, TuringMachineMove.STAY));
		}

	}

}
