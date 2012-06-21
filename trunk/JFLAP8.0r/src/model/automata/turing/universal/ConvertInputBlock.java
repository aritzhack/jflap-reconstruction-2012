package model.automata.turing.universal;

import java.util.Map;
import java.util.TreeMap;

import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.library.BaseBlockTMBlock;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class ConvertInputBlock extends MappingBlock{

	private SymbolString myTransitionEncoding;
	
	public ConvertInputBlock(SymbolString transEncoding, TapeAlphabet alph, 
			BlankSymbol blank, int id) {
		super(alph, blank, "Convert", id);
		myTransitionEncoding = transEncoding;
	
	}
	
	private void addTransition(Block from, Block to, Symbol input) {
		TransitionSet<BlockTransition> trans = this.getTuringMachine().getTransitions();
		trans.add(new BlockTransition(from, to, input));
	}

	@Override
	public void updateTuringMachine(Map<Symbol, SymbolString> encodings) {

		
	}

}
