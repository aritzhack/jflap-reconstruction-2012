package model.automata.turing.buildingblock.library;

import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.TuringMachineTransition;
import model.formaldef.components.symbols.Symbol;

public class MoveBlock extends BaseBlock {
	private TuringMachineMove myDirection;
	
	public MoveBlock(TuringMachineMove direction, TapeAlphabet alph, BlankSymbol blank, int id) {
		super(alph, blank, createName(direction), id);
		
		myDirection = direction;

		addStartAndFinalStates();
		
		updateTuringMachine(alph);
	}

	private static String createName(TuringMachineMove direction){
		String move = "Move_"+direction.char_abbr;
		return move;
	}
	
	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		TransitionSet<TuringMachineTransition> transitions = getTuringMachine().getTransitions();
		transitions.clear();
		
		State start = getTuringMachine().getStartState();
		State finish = getTuringMachine().getFinalStateSet().first();
		
		for(Symbol term : tape){
			transitions.add(new TuringMachineTransition(start, finish, term, term, myDirection));
		}
	}
}
