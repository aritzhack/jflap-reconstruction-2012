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
import model.automata.turing.buildingblock.library.BlockLibrary;
import model.automata.turing.buildingblock.library.BlockTMUpdatingBlock;
import model.automata.turing.buildingblock.library.HaltBlock;
import model.automata.turing.buildingblock.library.MoveBlock;
import model.automata.turing.buildingblock.library.MoveUntilBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.automata.turing.buildingblock.library.WriteBlock;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class ToMapBlock extends BlockTMUpdatingBlock {
	int myLength;
	
	public ToMapBlock(SymbolString ones, TapeAlphabet alph, int id) {
		super(alph, "Unary"+BlockLibrary.UNDSCR+ones.size(), id, ones);
	}

	@Override
	public void constructFromBase(TapeAlphabet alph,
			TuringMachine localTM, Object... args) {
		SymbolString ones = (SymbolString) args[0];
		myLength = ones.size();
		BlankSymbol blank = new BlankSymbol();
		BlockTuringMachine tm = this.getTuringMachine();
		BlockSet blocks = tm.getStates();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		
		int id = 0;
		
		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		Block b2 = new WriteBlock(blank.getSymbol(), alph, id++);
		BlockTransition trans = new BlockTransition(b1, b2, new Symbol(TILDE));
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, blank.getSymbol(), alph, id++);
		trans = new BlockTransition(b1,b2, new Symbol(TILDE));
		transitions.add(trans);
		
		alph.add(new Symbol("1"));
		
		for(int i=0; i<myLength;i++){
			b1=b2;
			b2 = new WriteBlock(new Symbol("1"), alph, id++);
			trans = new BlockTransition(b1, b2, new Symbol(TILDE));
			transitions.add(trans);
			
			b1=b2;
			b2 = new MoveBlock(TuringMachineMove.RIGHT, alph, id++);
			trans = new BlockTransition(b1, b2, new Symbol(TILDE));
			transitions.add(trans);
		}
		
		b1=b2;
		b2 = new WriteBlock(new Symbol("0"), alph, id++);
		trans = new BlockTransition(b1, b2, new Symbol(TILDE));
		transitions.add(trans);
		
		b1=b2;
		b2 = new HaltBlock(id++);
		trans = new BlockTransition(b1, b2, new Symbol(TILDE));
		transitions.add(trans);
		
		tm.getFinalStateSet().add(b2);
	}

}
