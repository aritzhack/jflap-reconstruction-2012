package model.automata.turing.buildingblock.library;

import model.automata.State;
import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class DuplicateCharBlock extends BaseBlockTMBlock {

	private Symbol mySymbol;

	public DuplicateCharBlock(TapeAlphabet alph, BlankSymbol blank, int id, Symbol character) {
		super(alph, blank, BlockLibrary.DUPLICATE+character, id);
		
		mySymbol = character;
		
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		
		TapeAlphabet tapeAlph = tm.getTapeAlphabet();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		
		BlockSet blocks = tm.getStates();
		id = 0;
		
		Block b1 = new StartBlock(alph, blank, id++);
		tm.setStartState(b1);
		Block b2 = new WriteBlock(blank.getSymbol(), alph, blank, id++);
		BlockTransition trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, blank.getSymbol(), tapeAlph, blank, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new WriteBlock(mySymbol, alph, blank, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.LEFT, blank.getSymbol(), tapeAlph, blank, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new WriteBlock(mySymbol, alph, blank, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new FinalBlock(alph, blank, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		tm.getFinalStateSet().add(b2);
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		BlockSet blocks = getTuringMachine().getStates();
		
		for(State block : blocks){
			((BaseMultiTapeBlock) block).updateTuringMachine(tape);
		}
	}
}
