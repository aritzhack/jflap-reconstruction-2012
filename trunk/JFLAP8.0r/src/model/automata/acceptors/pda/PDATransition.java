package model.automata.acceptors.pda;

import java.util.Set;

import model.automata.State;
import model.automata.Transition;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;

public class PDATransition extends Transition {

	private Symbol myPop;
	private SymbolString myPush;

	public PDATransition(State from, State to, SymbolString input, Symbol pop, SymbolString push) {
		super(from, to, input);
		setPop(pop);
		setPush(push);
	}

	
	
	@Override
	public String getDescriptionName() {
		return "Pushdown Automaton Transition";
	}

	@Override
	public String getDescription() {
		return "This is a transition reserved for basic PDAs and any variation thereof. " +
				"This transition function maps an input SymbolString, State, and stack symbol to " +
				"a next state and symbols to push onto the stack.";
	}



	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		Set<Symbol> used = super.getUniqueSymbolsUsed();
		used.add(this.getPop());
		used.addAll(this.getPush());
		return used;
	}



	@Override
	public boolean purgeOfSymbol(Symbol s) {
		
		boolean purgePop = s.equals(this.getPop());
		if (purgePop)
			s.setString("");
		
		boolean purgePush = this.getPush().purgeOfSymbol(s);
		
		return super.purgeOfSymbol(s) || purgePop || purgePush;
	}



	public Symbol getPop() {
		return myPop;
	}



	public void setPop(Symbol pop) {
		this.myPop = pop;
	}



	public SymbolString getPush() {
		return myPush;
	}



	public void setPush(SymbolString push) {
		this.myPush = push;
	}

}
