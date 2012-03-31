package model.automata.acceptors.fsa;

import java.util.Set;

import model.automata.Transition;
import model.automata.TransitionLabel;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;

public class FSATransitionLabel extends TransitionLabel {

	private SymbolString myRead;
	
	public FSATransitionLabel() {
		this(new SymbolString());
	}
	
	public FSATransitionLabel(SymbolString read) {
		setRead(read);
	}

	@Override
	public int compareTo(TransitionLabel o) {
		
		return this.getRead().compareTo(((FSATransitionLabel) o).getRead());
	}

	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		return this.getRead().getUniqueSymbolsUsed();
	}

	@Override
	public TransitionLabel clone() {
		return new FSATransitionLabel(this.getRead());
	}

	@Override
	public String toString() {
		return myRead.toString();
	}

	@Override
	public SymbolString[] getArray() {
		return new SymbolString[]{myRead};
	}

	public SymbolString getRead(){
		return myRead;
	}
	
	public void setRead(SymbolString read){
		myRead = read;
	}

}
