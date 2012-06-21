package model.automata.turing.buildingblock.library.universal;

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

public class ConvertToUniversalBlock extends BaseBlockTMBlock{

	private Map<Symbol, SymbolString> myMappings;
	
	public ConvertToUniversalBlock(TapeAlphabet alph, BlankSymbol blank,
			String name, int id) {
		super(alph, blank, name, id);
		myMappings = new TreeMap<Symbol, SymbolString>();
		
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		
	}
	
	private void addTransition(Block from, Block to, Symbol input) {
		TransitionSet<BlockTransition> trans = this.getTuringMachine().getTransitions();
		trans.add(new BlockTransition(from, to, input));
	}

}
