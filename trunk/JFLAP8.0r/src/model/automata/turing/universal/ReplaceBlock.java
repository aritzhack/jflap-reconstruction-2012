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
import model.automata.turing.buildingblock.library.MoveUntilBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.automata.turing.buildingblock.library.WriteBlock;
import model.symbols.Symbol;

public class ReplaceBlock extends BlockTMUpdatingBlock {

	public ReplaceBlock(TuringMachineMove direction, Symbol symbol, TapeAlphabet alph, int id) {
		super(alph, "Replace_"+symbol.getString()+"_"+direction.char_abbr, id, direction, symbol);
		
	}

	@Override
	public void constructFromBase(TapeAlphabet parentAlph,
			TuringMachine localTM, Object... args) {
		
		TuringMachineMove direction = (TuringMachineMove) args[0];
		Symbol symbol = (Symbol) args[1];
		int id =0;
		
		BlockTuringMachine tm = getTuringMachine();
		TapeAlphabet tape = tm.getTapeAlphabet();
		BlankSymbol blank = new BlankSymbol();
		Symbol hash = new Symbol(TM_MARKER), tilde = new Symbol(TILDE);
		
		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		Block b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, hash, tape, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveUntilBlock(direction, blank.getSymbol(), tape, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2; 
		b2 = new WriteBlock(symbol, tape, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new HaltBlock(id++);
		addTransition(b1, b2, tilde);
		tm.getFinalStateSet().add(b2);
	}
	

}
