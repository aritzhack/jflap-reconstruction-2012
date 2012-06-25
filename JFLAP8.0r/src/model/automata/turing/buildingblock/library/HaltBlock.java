package model.automata.turing.buildingblock.library;

import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;

public class HaltBlock extends StartHaltBlock {

	public HaltBlock(int id) {
		super(BlockLibrary.FINAL, id);
		
	}

}
