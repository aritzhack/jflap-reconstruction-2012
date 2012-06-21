package model.automata.turing.buildingblock.library;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.formaldef.components.symbols.Symbol;

/**
 * Sample building block
 * 
 * @author Ian McMahon
 * 
 */
public class MoveUntilBlock extends BaseMultiTapeBlock {
	private MultiTapeTMTransition myFinalTransition;
	private Symbol mySymbol;
	private TuringMachineMove myMove;
	
	public MoveUntilBlock(TuringMachineMove direction, Symbol read,
			TapeAlphabet alph, BlankSymbol blank, int id) {
		super(alph, blank, createName(direction, read), id);
		
		mySymbol = read;
		myMove = direction;
	
		addStartAndFinalStates();
		
		TuringMachine tm = getTuringMachine();
		StateSet states = tm.getStates();
		
		State start = tm.getStartState();
		State intermediateState = states.createAndAddState();
		State finalState = tm.getFinalStateSet().first();
		
		myFinalTransition = new MultiTapeTMTransition(intermediateState, 
				finalState, read, read, TuringMachineMove.STAY);
		
		tm.getTransitions().add(myFinalTransition);
		updateTuringMachine(alph);
	}
	
	private static String createName(TuringMachineMove direction, Symbol read){
		if(direction.equals(TuringMachineMove.STAY)) throw new RuntimeException("Infinite loops are fun, but not allowed");
		String move = BlockLibrary.MOVE + BlockLibrary.UNDSCR +
				direction.char_abbr + BlockLibrary.UNDSCR + 
				BlockLibrary.UNTIL + BlockLibrary.UNDSCR + read;
		return move;
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		TuringMachine tm = getTuringMachine();
		StateSet states = tm.getStates();
		
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		transitions.clear();
		transitions.add(myFinalTransition);
		
		State start = tm.getStartState();
		State intermediate = states.getStateWithID(2);
		
		for(Symbol term : tape){
			transitions.add(new MultiTapeTMTransition(start, intermediate, term, term, myMove));
			
			if(!term.equals(mySymbol)){
				transitions.add(new MultiTapeTMTransition(intermediate, intermediate, term, term, myMove));
			}
		}
	}
}
