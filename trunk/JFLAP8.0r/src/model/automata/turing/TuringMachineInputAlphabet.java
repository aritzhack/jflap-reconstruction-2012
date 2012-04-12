package model.automata.turing;

import java.util.Set;

import model.automata.InputAlphabet;
import model.formaldef.UsesSymbols;
import model.formaldef.components.symbols.Symbol;

public class TuringMachineInputAlphabet extends InputAlphabet implements UsesSymbols{

	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		return this;
	}

	@Override
	public boolean purgeOfSymbol(Symbol s) {
		return this.remove(s);
	}

}
