package model.automata.transducers.moore;

import model.automata.State;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.automata.transducers.OutputFunction;
import model.formaldef.components.symbols.SymbolString;

public class MooreOutputFunction extends OutputFunction {

	public MooreOutputFunction(State s,SymbolString output) {
		super(s, output);
	}

	@Override
	public String getDescriptionName() {
		return "Moore OutputFunction";
	}


	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean matches(FiniteStateTransition trans) {
		return trans.getToState().equals(this.getState());
	}

}
