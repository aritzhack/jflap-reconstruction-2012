package model.automata.turing;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import model.automata.State;
import model.automata.StateSet;
import model.formaldef.UsesSymbols;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import preferences.JFLAPPreferences;

public class BuildingBlockSet extends StateSet implements UsesSymbols{
	
	public BuildingBlock getStateWithID(int id) {
		for (State s: this){
			if (s.getID() == id)
				return (BuildingBlock) s;
		}
		
		return null;
	}

	public BuildingBlock createAndAddState() {
		return createAndAddState(null);
	}
	
	public BuildingBlock createAndAddState(TuringMachine tm){
		int id = this.getNextUnusedID();
		BuildingBlock s = new BuildingBlock(tm, JFLAPPreferences.getDefaultStateNameBase()+id, id);
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
		return false;
	}
}
