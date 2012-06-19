package model.automata.turing.buildingblock.library;

import model.automata.State;
import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineTransition;
import model.formaldef.components.symbols.Symbol;

public class StartBlock extends HaltBlock {

	public StartBlock(TapeAlphabet alph, BlankSymbol blank, int id) {
		super(alph, blank, "Start", id);
	}

}
