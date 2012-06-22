package model.automata.turing.universal;

import java.util.Map;
import java.util.TreeMap;

import debug.JFLAPDebug;

import preferences.JFLAPPreferences;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.buildingblock.library.BaseBlockTMBlock;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public abstract class MappingBlock extends BaseBlockTMBlock {

	public MappingBlock(TapeAlphabet alph, BlankSymbol blank, String name,
			int id) {
		super(alph, blank, name, id);
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		updateTuringMachine(createMap(tape));
	}

	
	public abstract void updateTuringMachine(Map<Symbol, SymbolString> encodingMap);

	public static Map<Symbol, SymbolString> createMap(TapeAlphabet alph){
		Map<Symbol, SymbolString> map = new TreeMap<Symbol, SymbolString>();
		Symbol[] symbols = alph.toArray(new Symbol[0]);
		Symbol one = new Symbol("1");
		for (int i = 0; i < alph.size(); i++){
			SymbolString ones = new SymbolString();
			
			for (int j = 0; j<i+2; j++)
				ones.add(one);
			
			map.put(symbols[i], ones);
		}
		
		map.put(JFLAPPreferences.getTMBlankSymbol(), new SymbolString(one));
		return map;
	}
}
