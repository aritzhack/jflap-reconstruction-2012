package model.automata.turing.buildingblock.library.universal;

import java.util.Map;
import java.util.TreeMap;

import preferences.JFLAPPreferences;

import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.UniversalTuringMachine;
import model.automata.turing.buildingblock.Block;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class UniversalTMBlock extends Block {

	public UniversalTMBlock(UniversalTuringMachine machine, String name, int id) {
		super(machine, name, id);
	}
	public UniversalTMBlock(String states, String trans, String name, int id){
		this(new UniversalTuringMachine(states, trans, true), name,id);
	}
	
	
	public static Map<Symbol, SymbolString> createMap(TapeAlphabet alph){
		Map<Symbol, SymbolString> map = new TreeMap<Symbol, SymbolString>();
		Symbol[] symbols = alph.toArray(new Symbol[0]);
		Symbol one = new Symbol("1");
		for (int i = 2; i < alph.size(); i++){
			SymbolString ones = new SymbolString();
			
			for (int j = 0; j<i; j++)
				ones.add(one);
			
			map.put(symbols[i-2], ones);
		}
		
		map.put(JFLAPPreferences.getTMBlankSymbol(), new SymbolString(one));
		return map;
	}

}
