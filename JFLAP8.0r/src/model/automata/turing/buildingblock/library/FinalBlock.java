package model.automata.turing.buildingblock.library;

import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;

public class FinalBlock extends HaltBlock {

	public FinalBlock(TapeAlphabet alph, BlankSymbol blank,int id) {
		super(alph, blank, "Final", id);
		// TODO Auto-generated constructor stub
	}

}
