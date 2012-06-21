package model.automata.turing.universal;

import java.util.Map;
import java.util.TreeMap;

import preferences.JFLAPPreferences;

import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.buildingblock.Block;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class UniversalTMBlock extends Block {

	public UniversalTMBlock(int id){
		super(new UniversalTuringMachine(true), "UnivTM" ,id);
	}
	
	


}
