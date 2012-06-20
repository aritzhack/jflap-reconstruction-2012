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

public class MoveBlock extends BaseMultiTapeBlock {
	private TuringMachineMove myDirection;
	
	public MoveBlock(TuringMachineMove direction, TapeAlphabet alph, BlankSymbol blank, int id) {
		super(alph, blank, createName(direction), id);
		
		myDirection = direction;

		addStartAndFinalStates();
		
		updateTuringMachine(alph);
	}

	private static String createName(TuringMachineMove direction){
		String move = BlockLibrary.MOVE + BlockLibrary.UNDSCR +direction.char_abbr;
		return move;
	}
	
	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		TransitionSet<MultiTapeTMTransition> transitions = getTuringMachine().getTransitions();
		transitions.clear();
		
		State start = getTuringMachine().getStartState();
		State finish = getTuringMachine().getFinalStateSet().first();
		
		for(Symbol term : tape){
			transitions.add(new MultiTapeTMTransition(start, finish, term, term, myDirection));
		}
	}
}
