package model.automata.acceptors.pda;

import model.automata.Transition;
import model.automata.TransitionFunctionSet;
import model.formaldef.components.symbols.Symbol;

public class PDATransitionSet extends TransitionFunctionSet<PDATransition> {
	
	
	
	public void purgeofStackSymbol(Symbol s){
		for (PDATransition t: this){
			t.getPop().purgeOfSymbol(s);
			t.getPush().purgeOfSymbol(s);
		}
	}
	
}
