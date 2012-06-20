package file.xml.formaldef.automata;

import java.util.List;
import java.util.Map;

import util.UtilFunctions;

import debug.JFLAPDebug;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.BlockLibrary;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import file.xml.formaldef.components.functions.transitions.TransitionSetTransducer;
import file.xml.formaldef.components.functions.transitions.tm.BlockTransitionTransducer;

public class BlockTMTransducer extends AutomatonTransducer<BlockTuringMachine> {

	@Override
	public String getTag() {
		return BLOCK_TM_TAG;
	}

	@Override
	public TransitionSetTransducer createTransitionTransducer(
			List<Alphabet> alphs) {
		InputAlphabet input = retrieveAlphabet(alphs, InputAlphabet.class);

		BlockTransitionTransducer ducer = new BlockTransitionTransducer(input);
		
		TransitionSetTransducer<BlockTransition> trans = 
				new TransitionSetTransducer<BlockTransition>(ducer);
		
		return trans;
	}

	@Override
	public BlockTuringMachine buildStructure(Object[] subComp) {
		BlockSet blocks = retrieveTarget(BlockSet.class, subComp);
		TapeAlphabet tapeAlph = retrieveTarget(TapeAlphabet.class, subComp);

		convertAnyLibraryBlocks(blocks, tapeAlph);
		TransitionSet<BlockTransition> oldtrans = retrieveTarget(TransitionSet.class, subComp);
		StartState oldStart = retrieveTarget(StartState.class, subComp);

		TransitionSet<BlockTransition> transitions = createTransitionSet(blocks,oldtrans);
		StartState start = createStartBlock(oldStart, blocks);
		
		return new BlockTuringMachine(
						blocks,
						tapeAlph,
						retrieveTarget(BlankSymbol.class, subComp),
						retrieveTarget(InputAlphabet.class, subComp),
						transitions,
						start,
						retrieveTarget(FinalStateSet.class, subComp));
	}

	private void convertAnyLibraryBlocks(BlockSet blocks, TapeAlphabet tapeAlph) {
		for (State b: blocks.toArray(new State[0])){
			Block fromLib = BlockLibrary.createFromName(b.getName(), tapeAlph, b.getID());
			blocks.remove(b);
			if (fromLib != null){
				b = fromLib;
			}
			blocks.add(b);
		}
	}

	private StartState createStartBlock(StartState oldStart, BlockSet blocks) {
		State old = oldStart.getState();
		Block start = blocks.getStateWithID(old.getID());
		return new StartState(start);
	}

	private TransitionSet<BlockTransition> createTransitionSet(BlockSet blocks,
			TransitionSet<BlockTransition> oldtrans) {
		TransitionSet<BlockTransition> transitions = new TransitionSet<BlockTransition>();
		for (BlockTransition trans: oldtrans){
			State oldFrom = trans.getFromState();
			State oldTo = trans.getToState();
			SymbolString input = new SymbolString(trans.getInput());
			
			Block from = blocks.getStateWithID(oldFrom.getID());
			Block to = blocks.getStateWithID(oldTo.getID());
			transitions.add(new BlockTransition(from, to, input));
		}
		return transitions;
	}


}
