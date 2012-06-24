package model.automata.turing.universal;

import java.util.Map;
import java.util.TreeMap;

import universe.preferences.JFLAPPreferences;

import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.buildingblock.Block;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class UniversalTMBlock extends Block {

	public UniversalTMBlock(int id){
		super(new UniversalTuringMachine(true), "UnivTM" ,id);
	}
	
	


}
