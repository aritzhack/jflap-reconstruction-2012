package model.automata.turing;

import java.util.Collection;
import java.util.Set;

import model.automata.State;
import model.formaldef.UsesSymbols;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;

public class BuildingBlock extends State implements UsesSymbols{
	private TuringMachine myMachine;

	public BuildingBlock(String name, int id) {
		this(null, name, id);
	}

	public BuildingBlock(TuringMachine machine, String name, int id){
		super(name, id);
		myMachine = machine.copy();
	}
	
	public void setTuringMachine(TuringMachine tm){
		myMachine = tm;
	}
	
	public TuringMachine getTuringMachine(){
		return myMachine.copy();
	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		return myMachine.getSymbolsUsedForAlphabet(a);
	}

	@Override
	public boolean applySymbolMod(String from, String to) {
		return myMachine.applySymbolMod(from, to);
	}

	@Override
	public boolean purgeOfSymbols(Alphabet a, Collection<Symbol> s) {
		return myMachine.purgeOfSymbols(a, s);
	}
	
	
}
