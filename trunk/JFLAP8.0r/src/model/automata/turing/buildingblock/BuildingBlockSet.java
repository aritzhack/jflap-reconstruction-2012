package model.automata.turing.buildingblock;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import model.automata.State;
import model.automata.StateSet;
import model.automata.turing.TuringMachine;
import model.formaldef.FormalDefinitionException;
import model.formaldef.UsesSymbols;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import preferences.JFLAPPreferences;

public class BuildingBlockSet extends StateSet implements UsesSymbols{
	
	@Override
	public BuildingBlock getStateWithID(int id) {
		return (BuildingBlock) super.getStateWithID(id);
	}

	@Deprecated
	public BuildingBlock createAndAddState() {
		throw new RuntimeException("Don't use this method, use createAndAddState(TuringMachine tm) instead");
	}
	
	public BuildingBlock createAndAddState(TuringMachine tm, String name){
		int id = this.getNextUnusedID();
		BuildingBlock s = new BuildingBlock(tm, name, id);
		this.add(s);
		return s;
	}
	
	@Override
	public BuildingBlockSet copy() {
		return (BuildingBlockSet) super.copy();
	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		Set<Symbol> symbolAlphabet = new TreeSet<Symbol>();
		for(State block : this){
			symbolAlphabet.addAll(((BuildingBlock) block).getSymbolsUsedForAlphabet(a));
		}
		return symbolAlphabet;
	}

	@Override
	public boolean applySymbolMod(String from, String to) {
		boolean changed = false;
		for(State block : this){
			if(((BuildingBlock) block).applySymbolMod(from, to)){
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean purgeOfSymbols(Alphabet a, Collection<Symbol> s) {
		boolean changed = false;
		for(State block : this){
			if(((BuildingBlock) block).purgeOfSymbols(a, s)){
				changed = true;
			}
		}
		return changed;
	}
}
