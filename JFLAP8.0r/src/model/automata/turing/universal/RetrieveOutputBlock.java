package model.automata.turing.universal;

import java.util.Map;

import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.buildingblock.library.BaseBlockTMBlock;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class RetrieveOutputBlock extends MappingBlock{

	public RetrieveOutputBlock(TapeAlphabet alph, BlankSymbol blank,
			int id) {
		super(alph, blank, "Translate", id);
	}

	@Override
	public void updateTuringMachine(Map<Symbol, SymbolString> encodingMap) {
		
	}

}
