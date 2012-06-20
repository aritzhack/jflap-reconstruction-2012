package model.automata.turing.buildingblock.library;

import model.automata.State;
import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.formaldef.components.symbols.Symbol;

public class MoveUntilNotBlock extends BaseMultiTapeBlock{

	private MultiTapeTMTransition myNotTransition;
	private Symbol mySymbol;
	
	public MoveUntilNotBlock(TuringMachineMove direction, Symbol read,
			TapeAlphabet alph, BlankSymbol blank, int id) {
		super(alph, blank, createName(direction, read), id);
		
		mySymbol = read;
	
		addStartAndFinalStates();
		
		TuringMachine tm = getTuringMachine();
		
		State start = tm.getStartState();
		
		myNotTransition = new MultiTapeTMTransition(start, 
				start, read, read, direction);
		
		tm.getTransitions().add(myNotTransition);
		updateTuringMachine(alph);
	}
	
	private static String createName(TuringMachineMove direction, Symbol read){
		if(direction.equals(TuringMachineMove.STAY)) throw new RuntimeException("Infinite loops are fun, but not allowed");
		String move = BlockLibrary.MOVE + BlockLibrary.UNDSCR +
				direction.char_abbr + BlockLibrary.UNDSCR + BlockLibrary.UNTIL +
				BlockLibrary.UNDSCR + BlockLibrary.NOT + BlockLibrary.UNDSCR+ read;
		return move;
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		TransitionSet<MultiTapeTMTransition> transitions = getTuringMachine().getTransitions();
		transitions.clear();
		transitions.add(myNotTransition);
		
		State start = getTuringMachine().getStartState();
		State finish = getTuringMachine().getFinalStateSet().first();
		
		for(Symbol term : tape){
			if(!term.equals(mySymbol)){
				transitions.add(new MultiTapeTMTransition(start, finish, term, term, TuringMachineMove.STAY));
			}
		}
	}

}
