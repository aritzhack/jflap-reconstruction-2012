package model.automata.transducers.moore;

import java.util.Set;

import model.automata.State;
import model.automata.StateSet;
import model.formaldef.UsesSymbols;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;

public class MooreStateSet extends StateSet implements UsesSymbols {

	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		return null;
	}

	@Override
	public boolean purgeOfSymbol(Symbol s) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean add(State e) {
		return this.add(new MooreState(e.getName(), 
										e.getID(), 
										e.getLocation(),
										new SymbolString()));
	}
	
	public boolean add(MooreState state){
		return super.add(state);
	}
	

}
