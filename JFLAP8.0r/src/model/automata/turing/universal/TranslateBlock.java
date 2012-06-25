package model.automata.turing.universal;

import model.automata.State;
import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.BaseBlockTMBlock;
import model.automata.turing.buildingblock.library.HaltBlock;
import model.automata.turing.buildingblock.library.MoveBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.automata.turing.buildingblock.library.WriteBlock;
import model.symbols.Symbol;

public class TranslateBlock extends BaseBlockTMBlock {

	public TranslateBlock(TapeAlphabet alph, BlankSymbol blank,
			int id) {
		super(alph, blank, "Translate", id);
		
		BlockTuringMachine tm = getTuringMachine();
		BlockSet blocks = tm.getStates();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		
		id=0;
		Symbol tilde = new Symbol(TILDE);
		Symbol hash = new Symbol(TM_MARKER);
		
		Block b1 = new StartBlock(alph, blank, id++);
		tm.setStartState(b1);
		
		Block b2 = new WriteBlock(blank.getSymbol(), alph, blank, id++);
		BlockTransition trans = new BlockTransition(b1,b2,tilde);
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveBlock(TuringMachineMove.RIGHT, alph, blank, id++);
		trans = new BlockTransition(b1, b2, tilde);
		transitions.add(trans);
		
		b1=b2;
		b2 = new HaltBlock(alph, blank, id++);
		trans = new BlockTransition(b1, b2, tilde);
		transitions.add(trans);
		
		tm.getFinalStateSet().add(b2);
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		for(State block : getTuringMachine().getStates()){
			if(block instanceof BaseBlockTMBlock)
			((BaseBlockTMBlock) block).updateTuringMachine(tape);
		}
	}

}
