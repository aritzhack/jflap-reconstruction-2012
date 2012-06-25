package model.automata.turing.universal;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import oldnewstuff.main.JFLAP;

import debug.JFLAPDebug;

import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.HaltBlock;
import model.automata.turing.buildingblock.library.MoveUntilBlock;
import model.automata.turing.buildingblock.library.ShiftBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.automata.turing.buildingblock.library.WriteBlock;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class RetrieveOutputBlock extends MappingBlock{
	
	private Block rightPivot, leftPivot, rightFromState, leftFromState;
	private Symbol tilde = new Symbol(TILDE), hash = new Symbol(TM_MARKER), zero = new Symbol("0"), one = new Symbol("1");
	private Set<Block> loops;
	
	public RetrieveOutputBlock(TapeAlphabet alph, BlankSymbol blank,
			int id) {
		super(alph, blank, "Translate output", id);
		
		loops = new TreeSet<Block>();
		
		BlockTuringMachine tm = getTuringMachine();
		TapeAlphabet tape = tm.getTapeAlphabet();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		initMarkers(tape, blank, tm, transitions);
		initTranslates(tape);
		updateTuringMachine(tape);
		
	}

	private void initTranslates(TapeAlphabet alph) {
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		BlockSet blocks = tm.getStates();
		BlankSymbol blank = new BlankSymbol();
		int id = blocks.getNextUnusedID();
		
		Block rightOut, lastBlock = blocks.getStateWithID(id-1);
		
		Block b1 = rightPivot = new MoveUntilBlock(TuringMachineMove.LEFT, zero, alph, blank, id++);
		addTransition(lastBlock, b1, tilde);
		Block b2 = rightOut = new TranslateBlock(alph, blank, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = leftPivot = new MoveUntilBlock(TuringMachineMove.LEFT, zero, alph, blank, id++);
		addTransition(b1, b2, hash);
		
		b1=b2;
		b2 = new TranslateBlock(alph, blank, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = leftFromState = new TranslateBlock(alph, blank, id++);
		addTransition(b1, b2, one);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, hash, alph, blank, id++);
		addTransition(b1, b2, blank.getSymbol());
		
		b1=b2;
		b2 = new ShiftBlock(TuringMachineMove.LEFT, alph, blank, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new HaltBlock(alph, blank, id++);
		addTransition(b1, b2, tilde);
		tm.getFinalStateSet().add(b2);
		
		rightFromState = new TranslateBlock(alph, blank, id++);
		addTransition(rightOut, rightFromState, one);
	}

	private void initMarkers(TapeAlphabet alph, BlankSymbol blank,
			BlockTuringMachine tm, TransitionSet<BlockTransition> transitions) {
		int id = 0;
		
		Block b1 = new StartBlock(alph, blank, id++);
		tm.setStartState(b1);
		Block b2 = new WriteBlock(hash, alph, blank, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, blank.getSymbol(), alph, blank, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2; 
		b2 = new WriteBlock(hash, alph, blank, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.LEFT, blank.getSymbol(), alph, blank, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new WriteBlock(zero, alph, blank, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, hash, alph, blank, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new WriteBlock(one, alph, blank, id++);
		addTransition(b1, b2, tilde);
	
	}

	@Override
	public void updateTuringMachine(Map<Symbol, SymbolString> encodingMap) {
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		TapeAlphabet alph = tm.getTapeAlphabet();
		BlockSet blocks = tm.getStates();
		BlankSymbol blank = new BlankSymbol();
		
		Set<Symbol> tape = new TreeSet<Symbol>();
		for(Symbol s : encodingMap.keySet()){
			tape.add(s);
		}
		
		for(Block state : loops){
			blocks.remove(state);
		}
		
		loops.clear();
		translateBothSides(encodingMap);
	}

	private void translateBothSides(
			Map<Symbol, SymbolString> encodingMap) {
		BlockTuringMachine tm = getTuringMachine();
		BlockSet blocks = tm.getStates();
		TapeAlphabet tape = tm.getTapeAlphabet();
		BlankSymbol blank = new BlankSymbol();
		
		Block rightBlock1, rightBlock2 = rightFromState;
		Block leftBlock1, leftBlock2 = leftFromState;
		BlockTransition trans;
		int id = blocks.getNextUnusedID();
		
		for(int i=2; i<=encodingMap.size();i++){
			rightBlock1=rightBlock2; 
			rightBlock2 = new TranslateBlock(tape, blank, id++);
			addTransition(rightBlock1, rightBlock2, one);
			
			leftBlock1=leftBlock2;
			leftBlock2 = new TranslateBlock(tape, blank, id++);
			addTransition(leftBlock1, leftBlock2, one);
			
			Symbol a = getKeyForValue(encodingMap, i);
			Block replaceRight = new ReplaceBlock(TuringMachineMove.RIGHT, a, tape, blank, id++);
			addTransition(rightBlock2, replaceRight, zero);
			
			Block replaceLeft = new ReplaceBlock(TuringMachineMove.LEFT, a, tape, blank, id++);
			addTransition(leftBlock2, replaceLeft, blank.getSymbol());
			
			addTransition(replaceRight, rightPivot, tilde);
			addTransition(replaceLeft, leftPivot, tilde);
			
			loops.add(replaceRight);
			loops.add(rightBlock2);
			loops.add(replaceLeft);
			loops.add(leftBlock2);
		}

	}

	private Symbol getKeyForValue(Map<Symbol, SymbolString> encodingMap, int oneLength){
		for(Symbol s : encodingMap.keySet()){
			if(encodingMap.get(s).size()==oneLength)
				return s;
		}
		return null;
	}
	
	private void addTransition(Block from, Block to, Symbol input) {
		TransitionSet<BlockTransition> trans = this.getTuringMachine().getTransitions();
		trans.add(new BlockTransition(from, to, input));
	}

}
