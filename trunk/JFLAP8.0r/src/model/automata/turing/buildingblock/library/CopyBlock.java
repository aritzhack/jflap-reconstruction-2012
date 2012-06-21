package model.automata.turing.buildingblock.library;

import java.util.HashSet;
import java.util.Set;

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

public class CopyBlock extends BaseBlockTMBlock {

	private Symbol myMarker;
	private Block myPivot;
	private Set<Block> myLoops;

	public CopyBlock(TapeAlphabet alph, BlankSymbol blank, int id) {
		super(alph, blank, BlockLibrary.COPY, id);

		myMarker = new Symbol(getBestMarker(alph));
		myLoops = new HashSet<Block>();
		
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();

		TapeAlphabet tapeAlph = tm.getTapeAlphabet();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();

		BlockSet blocks = tm.getStates();
		id = 0;
		
		Block b1 = new StartBlock(tapeAlph, blank, id++);
		tm.setStartState(b1);
		Block b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, blank.getSymbol(), tapeAlph, blank, id++);
		BlockTransition trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new WriteBlock(myMarker, tapeAlph, blank, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.LEFT, blank.getSymbol(), tapeAlph, blank, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = myPivot = new MoveBlock(TuringMachineMove.RIGHT, tapeAlph, blank, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new ShiftBlock(TuringMachineMove.LEFT, tapeAlph, blank, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(myMarker));
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.LEFT, blank.getSymbol(), tapeAlph, blank, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new MoveBlock(TuringMachineMove.RIGHT, tapeAlph, blank, id++);
		trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		
		b1=b2;
		b2 = new HaltBlock(alph, blank, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		tm.getFinalStateSet().add(b2);
		
		updateTuringMachine(alph);
	}

	private String getBestMarker(TapeAlphabet alph) {
		return "#";
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		TapeAlphabet alph = tm.getTapeAlphabet();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		BlockSet blocks = tm.getStates();
		BlankSymbol blank = new BlankSymbol();
		
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
				DuplicateCharBlock newBlock = new DuplicateCharBlock(alph, blank, blocks.getNextUnusedID(), term);
				BlockTransition toLoop = new BlockTransition(myPivot, newBlock, new SymbolString(term));
				BlockTransition fromLoop = new BlockTransition(newBlock, myPivot, new SymbolString(new Symbol(TILDE)));
				transitions.add(toLoop);
				transitions.add(fromLoop);
				myLoops.add(newBlock);
			}
		}
	}

}
