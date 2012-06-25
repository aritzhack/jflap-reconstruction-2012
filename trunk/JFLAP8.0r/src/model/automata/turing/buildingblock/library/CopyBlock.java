package model.automata.turing.buildingblock.library;

import java.util.HashSet;
import java.util.Set;

import util.JFLAPConstants;

import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class CopyBlock extends BlockTMUpdatingBlock {

	private Symbol myMarker;
	private Block myPivot;
	private Set<Block> myLoops;

	public CopyBlock(TapeAlphabet alph, int id) {
		super(alph, BlockLibrary.COPY, id);
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		TapeAlphabet alph = tm.getTapeAlphabet();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		BlockSet blocks = tm.getStates();
		
		alph.retainAll(tape);
		alph.addAll(tape);
		alph.add(myMarker);
		
		for(BlockTransition transition : transitions){
			Block to = transition.getToState(), from = transition.getFromState();
			
			if(myLoops.contains(to) || myLoops.contains(from)){
				transitions.remove(transition);
				blocks.remove((myPivot.equals(to) ? from : to));  
			}
		}
		
		myLoops.clear();
		
		for(Symbol term : alph){
			if(!term.equals(myMarker)){
				DuplicateCharBlock newBlock = new DuplicateCharBlock(alph, blocks.getNextUnusedID(), term);
				BlockTransition toLoop = new BlockTransition(myPivot, newBlock, new SymbolString(term));
				BlockTransition fromLoop = new BlockTransition(newBlock, myPivot, new SymbolString(new Symbol(TILDE)));
				transitions.add(toLoop);
				transitions.add(fromLoop);
				myLoops.add(newBlock);
			}
		}
	}

	@Override
	public void constructFromBase(TapeAlphabet alph,
			TuringMachine localTM, Object... args) {
		myMarker = new Symbol(JFLAPConstants.TM_MARKER);
		myLoops = new HashSet<Block>();
		BlankSymbol blank = new BlankSymbol();
		
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();

		TapeAlphabet tapeAlph = tm.getTapeAlphabet();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();

		int id = 0;
		
		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		Block b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, blank.getSymbol(), tapeAlph, id++);
		BlockTransition trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new WriteBlock(myMarker, tapeAlph, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.LEFT, blank.getSymbol(), tapeAlph, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = myPivot = new MoveBlock(TuringMachineMove.RIGHT, tapeAlph, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new ShiftBlock(TuringMachineMove.LEFT, tapeAlph, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(myMarker));
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.LEFT, blank.getSymbol(), tapeAlph, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveBlock(TuringMachineMove.RIGHT, tapeAlph, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new HaltBlock(id++);
		trans = new BlockTransition(b1,b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		tm.getFinalStateSet().add(b2);
	}

}
