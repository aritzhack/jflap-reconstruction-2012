package model.automata.turing.universal;

import model.automata.State;
import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.BlockTMUpdatingBlock;
import model.automata.turing.buildingblock.library.HaltBlock;
import model.automata.turing.buildingblock.library.MoveBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.automata.turing.buildingblock.library.WriteBlock;
import model.symbols.Symbol;

public class TranslateBlock extends BlockTMUpdatingBlock {

	public TranslateBlock(TapeAlphabet tape, int id) {
		super(tape, "Translate", id);
	}

	@Override
	public void constructFromBase(TapeAlphabet parentAlph,
			TuringMachine localTM, Object... args) {
		
		BlockTuringMachine tm = getTuringMachine();
		TapeAlphabet alph = tm.getTapeAlphabet();
		BlockSet blocks = tm.getStates();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		BlankSymbol blank = new BlankSymbol();

		int id=0;
		Symbol tilde = new Symbol(TILDE);
		Symbol hash = new Symbol(TM_MARKER);
		
		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		
		Block b2 = new WriteBlock(blank.getSymbol(), alph, id++);
		BlockTransition trans = new BlockTransition(b1,b2,tilde);
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveBlock(TuringMachineMove.RIGHT, alph, id++);
		trans = new BlockTransition(b1, b2, tilde);
		transitions.add(trans);
		
		b1=b2;
		b2 = new HaltBlock(id++);
		trans = new BlockTransition(b1, b2, tilde);
		transitions.add(trans);
		
		tm.getFinalStateSet().add(b2);
	}

}
