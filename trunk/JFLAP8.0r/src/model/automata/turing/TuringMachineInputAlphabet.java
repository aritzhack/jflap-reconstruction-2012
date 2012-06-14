package model.automata.turing;

import java.util.Set;

import model.automata.InputAlphabet;
import model.formaldef.UsesSymbols;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;

public class TuringMachineInputAlphabet extends InputAlphabet implements UsesSymbols{

	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		return this;
	}

	@Override
	public boolean purgeOfSymbol(Alphabet a, Symbol s) {
		return this.remove(s);
	}
	
	@Override
	public TuringMachineInputAlphabet copy() {
		return (TuringMachineInputAlphabet) super.copy();
	}

	@Override
	public void applyModification(Symbol from, Symbol to) {
		this.modify(from, to);
	}

}
