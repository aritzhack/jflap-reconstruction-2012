package model.automata.turing.buildingblock.library;

import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;

public class HaltBlock extends StartFinalBlock {

	public HaltBlock(TapeAlphabet alph, BlankSymbol blank,int id) {
		super(alph, blank, BlockLibrary.FINAL, id);
		
	}

}
