package model.automata.turing.universal;

import java.util.Set;

import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.HaltBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class ConvertedUniversalTM extends BlockTuringMachine {

	private SymbolString myEncoding;

	public ConvertedUniversalTM(MultiTapeTuringMachine tm){
		this(getEncoding(tm), tm.getTapeAlphabet());
	}

	public ConvertedUniversalTM(SymbolString encoding, TapeAlphabet tapeAlphabet) {
		this(encoding, tapeAlphabet.toCopiedSet());
	}

	private ConvertedUniversalTM(SymbolString encoding, Set<Symbol> copiedSet) {
		this.getTapeAlphabet().addAll(copiedSet);
		constructMachine(encoding);
	}

	private void constructMachine(SymbolString encoding) {
		TapeAlphabet alph = getTapeAlphabet();
		BlankSymbol blank = new BlankSymbol();
		Block start = new StartBlock(alph, blank, 0);
		Block convertTo = new ConvertInputBlock(encoding, alph, blank, 1);
		Block univTM = new UniversalTMBlock(2);
		Block translateFrom = new RetrieveOutputBlock(alph, blank, 3);
		Block halt = new HaltBlock(alph, new BlankSymbol(), 4);

		Symbol tilde = new Symbol(TILDE);
		
		BlockTransition t1 = new BlockTransition(start, convertTo, tilde);
		BlockTransition t2 = new BlockTransition(convertTo, univTM, tilde);
		BlockTransition t3 = new BlockTransition(univTM, translateFrom, tilde);
		BlockTransition t4 = new BlockTransition(translateFrom, halt, tilde);

		TransitionSet<BlockTransition> transitions = this.getTransitions();
		for (BlockTransition t: new BlockTransition[]{t1,t2,t3,t4}){
			transitions.add(t);
		}
		this.setStartState(start);
		this.getFinalStateSet().add(halt);
		

		
	}

	private static SymbolString getEncoding(MultiTapeTuringMachine tm) {
		TMtoEncodingConversion conv = new  TMtoEncodingConversion(tm);
		conv.stepToCompletion();
		return conv.getEncoding();
	
	}
	
}