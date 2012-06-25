package model.automata.turing.buildingblock.library;

import model.automata.State;
import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.symbols.Symbol;

public class StartBlock extends StartHaltBlock {

	public StartBlock(int id) {
		super(BlockLibrary.START, id);
	}

}
