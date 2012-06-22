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
import model.automata.turing.buildingblock.library.BlockLibrary;
import model.automata.turing.buildingblock.library.HaltBlock;
import model.automata.turing.buildingblock.library.MoveBlock;
import model.automata.turing.buildingblock.library.MoveUntilBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.automata.turing.buildingblock.library.WriteBlock;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class ToMapBlock extends BaseBlockTMBlock {
	int myLength;
	
	public ToMapBlock(SymbolString ones, TapeAlphabet alph, BlankSymbol blank, int id) {
		super(alph, blank, "Unary"+BlockLibrary.UNDSCR+ones.size(), id);
		myLength = ones.size();
		
		BlockTuringMachine tm = this.getTuringMachine();
		BlockSet blocks = tm.getStates();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		
		id = 0;
		
		Block b1 = new StartBlock(alph, blank, id++);
		tm.setStartState(b1);
		Block b2 = new WriteBlock(blank.getSymbol(), alph, blank, id++);
		BlockTransition trans = new BlockTransition(b1, b2, new Symbol(TILDE));
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, blank.getSymbol(), alph, blank, id++);
		trans = new BlockTransition(b1,b2, new Symbol(TILDE));
		transitions.add(trans);
		
		alph.add(new Symbol("1"));
		
		for(int i=0; i<myLength;i++){
			b1=b2;
			b2 = new WriteBlock(new Symbol("1"), alph, blank, id++);
			trans = new BlockTransition(b1, b2, new Symbol(TILDE));
			transitions.add(trans);
			
			b1=b2;
			b2 = new MoveBlock(TuringMachineMove.RIGHT, alph, blank, id++);
			trans = new BlockTransition(b1, b2, new Symbol(TILDE));
			transitions.add(trans);
		}
		
		b1=b2;
		b2 = new WriteBlock(new Symbol("0"), alph, blank, id++);
		trans = new BlockTransition(b1, b2, new Symbol(TILDE));
		transitions.add(trans);
		
		b1=b2;
		b2 = new HaltBlock(alph, blank, id++);
		trans = new BlockTransition(b1, b2, new Symbol(TILDE));
		transitions.add(trans);
		
		tm.getFinalStateSet().add(b2);
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		BlockSet blocks = getTuringMachine().getStates();
		
		for(State block : blocks){
			if(block instanceof BaseBlockTMBlock)
			((BaseBlockTMBlock) block).updateTuringMachine(tape);
		}

	}

}
