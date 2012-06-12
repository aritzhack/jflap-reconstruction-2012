package model.automata.simulate;

import oldnewstuff.util.Copyable;
import model.automata.Automaton;
import model.formaldef.Describable;
import model.formaldef.components.symbols.SymbolString;

public abstract class AutomatonSimulator implements Describable, Copyable{

	private Automaton myAutomaton;
	
	public AutomatonSimulator(Automaton a){
		myAutomaton = a;
	}
	
	public abstract void beginSimulation(SymbolString[] ... input);
	
	public abstract int getSpecialAcceptCase();
	
	public Automaton getAutomaton() {
		return myAutomaton;
	}

}
