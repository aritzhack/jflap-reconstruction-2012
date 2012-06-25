package model.automata.turing.buildingblock.library;

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
import model.automata.turing.buildingblock.UpdatingBlock;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class DuplicateCharBlock extends BlockTMUpdatingBlock {

	private Symbol mySymbol;

	public DuplicateCharBlock(TapeAlphabet alph, int id, Symbol character) {
		super(alph, BlockLibrary.DUPLICATE+ BlockLibrary.UNDSCR + character, id, character);
		
		
	}

	@Override
	public void constructFromBase(TapeAlphabet alph,
			TuringMachine localTM, Object... args) {
		
		mySymbol = (Symbol) args[0];
		BlankSymbol blank = new BlankSymbol();
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		
		TapeAlphabet tapeAlph = tm.getTapeAlphabet();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		
		BlockSet blocks = tm.getStates();
		int id = 0;
		
		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		Block b2 = new WriteBlock(blank.getSymbol(), alph, id++);
		BlockTransition trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, blank.getSymbol(), tapeAlph, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new WriteBlock(mySymbol, alph,  id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.LEFT, blank.getSymbol(), tapeAlph,  id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new WriteBlock(mySymbol, alph,  id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new HaltBlock(id++);
		trans = new BlockTransition(b1,b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		tm.getFinalStateSet().add(b2);
	}

}
