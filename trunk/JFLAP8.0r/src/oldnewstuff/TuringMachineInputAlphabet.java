package oldnewstuff;

import java.util.Collection;
import java.util.Set;

import model.automata.InputAlphabet;
import model.formaldef.UsesSymbols;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;

public class TuringMachineInputAlphabet extends InputAlphabet implements UsesSymbols{

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		return this;
	}

	@Override
	public boolean purgeOfSymbols(Alphabet a, Collection<Symbol> s) {
		return this.remove(s);
	}
	
	@Override
	public TuringMachineInputAlphabet copy() {
		return (TuringMachineInputAlphabet) super.copy();
	}

}
