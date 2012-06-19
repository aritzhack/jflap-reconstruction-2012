package model.automata.turing.buildingblock.library;

import model.automata.State;
import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.TuringMachineTransition;
import model.formaldef.components.symbols.Symbol;

public class MoveUntilNotBlock extends BaseBlock{

	private TuringMachineTransition myNotTransition;
	private Symbol mySymbol;
	
	public MoveUntilNotBlock(TuringMachineMove direction, BlankSymbol blank,
			Symbol read, TapeAlphabet alph, int id) {
		super(alph, blank, createName(direction, read), id);
		
		mySymbol = read;
	
		addStartAndFinalStates();
		
		TuringMachine tm = getTuringMachine();
		
		State start = tm.getStartState();
		
		myNotTransition = new TuringMachineTransition(start, 
				start, read, read, direction);
		
		tm.getTransitions().add(myNotTransition);
		updateTuringMachine(alph);
	}
	
	private static String createName(TuringMachineMove direction, Symbol read){
		if(direction.equals(TuringMachineMove.STAY)) throw new RuntimeException("Infinite loops are fun, but not allowed");
		String move = "Move_"+direction.char_abbr+"_Until_Not_"+read;
		return move;
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		TransitionSet<TuringMachineTransition> transitions = getTuringMachine().getTransitions();
		transitions.clear();
		transitions.add(myNotTransition);
		
		State start = getTuringMachine().getStartState();
		State finish = getTuringMachine().getFinalStateSet().first();
		
		for(Symbol term : tape){
			if(!term.equals(mySymbol)){
				transitions.add(new TuringMachineTransition(start, finish, term, term, TuringMachineMove.STAY));
			}
		}
	}

}
