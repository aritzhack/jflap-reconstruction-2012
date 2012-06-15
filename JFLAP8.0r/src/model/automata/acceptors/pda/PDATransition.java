package model.automata.acceptors.pda;

import java.util.Set;

import model.automata.State;
import model.automata.SingleInputTransition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class PDATransition extends SingleInputTransition<PDATransition> {

	private SymbolString myPop;
	private SymbolString myPush;

	public PDATransition(State from, State to, SymbolString input, SymbolString pop, SymbolString push) {
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
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		Set<Symbol> used = super.getSymbolsUsedForAlphabet(a);
		used.addAll(this.getPop());
		used.addAll(this.getPush());
		return used;
	}


	public SymbolString getPop() {
		return myPop;
	}



	public void setPop(SymbolString pop) {
		this.myPop = pop;
	}



	public SymbolString getPush() {
		return myPush;
	}



	public void setPush(SymbolString push) {
		this.myPush = push;
	}

	public SymbolString[] toArray(){
		return new SymbolString[]{this.getInput(),
									this.getPop(),
									this.getPush()};
	}
	
	@Override
	public int compareTo(PDATransition o) {
		int comp;
		if ((comp = super.compareTo(o)) != 0) return comp;
		
		PDATransition other = (PDATransition) o;
		
		
		SymbolString[] mine = this.toArray(),
						yours = other.toArray();
		
		
		for (int i = 0; i < mine.length; i++){
			comp = mine[i].compareTo(yours[i]);
			if (comp != 0) return comp;
				
		}
		
		return 0;
	}
	
	@Override
	public PDATransition copy() {
		return new PDATransition(this.getFromState().copy(), 
									this.getToState().copy(), 
									this.getInput().copy(),
									this.getPop().copy(), 
									this.getPush().copy());
	}



	@Override
	public String getLabelText() {
		return this.getInput() + "|" + this.getPop() + ";" + this.getPush();
	}



	
	@Override
	public SymbolString[] getPartsForAlphabet(Alphabet a) {
		if (a instanceof StackAlphabet){
			return new SymbolString[]{this.getPop(), this.getPush()};
		}
		return super.getPartsForAlphabet(a);
	}



	@Override
	public boolean isLambdaTransition() {
		
		return false;
	}
	
	

}
