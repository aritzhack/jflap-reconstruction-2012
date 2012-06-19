package model.automata.turing.buildingblock.library;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.TuringMachineTransition;
import model.formaldef.components.symbols.Symbol;

/**
 * Sample building block
 * 
 * @author Ian McMahon
 * 
 */
public class MoveUntilBlock extends BaseBlock {
	private TuringMachineTransition myFinalTransition;
	private Symbol mySymbol;
	private TuringMachineMove myMove;
	
	public MoveUntilBlock(TuringMachineMove direction, BlankSymbol blank,
			Symbol read, TapeAlphabet alph, int id) {
		super(alph, blank, createName(direction, read), id);
		
		mySymbol = read;
		myMove = direction;
	
		addStartAndFinalStates();
		
		TuringMachine tm = getTuringMachine();
		
		State start = tm.getStartState();
		State finalState = tm.getFinalStateSet().first();
		
		myFinalTransition = new TuringMachineTransition(start, 
				finalState, read, read, TuringMachineMove.STAY);
		
		tm.getTransitions().add(myFinalTransition);
		updateTuringMachine(alph);
	}
	
	private static String createName(TuringMachineMove direction, Symbol read){
		if(direction.equals(TuringMachineMove.STAY)) throw new RuntimeException("Infinite loops are fun, but not allowed");
		String move = "Move_"+direction.char_abbr+"_Until_"+read;
		return move;
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		TransitionSet<TuringMachineTransition> transitions = getTuringMachine().getTransitions();
		transitions.clear();
		transitions.add(myFinalTransition);
		
		State start = getTuringMachine().getStartState();
		
		for(Symbol term : tape){
			if(!term.equals(mySymbol)){
				transitions.add(new TuringMachineTransition(start, start, term, term, myMove));
			}
		}
	}
}
