package model.automata.transducers.mealy;

import model.automata.State;
import model.automata.acceptors.fsa.FSTransition;
import model.automata.transducers.OutputFunction;
import model.formaldef.components.symbols.SymbolString;

public class MealyOutputFunction extends OutputFunction {

	private SymbolString myInput;

	public MealyOutputFunction(State s,  SymbolString input, SymbolString output) {
		super(s, output);
		myInput = input;
	}

	@Override
	public String getDescriptionName() {
		return "Mealy Output Function";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public SymbolString getInput(){
		return myInput;
	}
	
	public void setInput(SymbolString s){
		myInput = s;
	}
	
	@Override
	public boolean matches(FSTransition trans) {
		return this.getInput().equals(trans.getInput()) &&
				this.getState().equals(trans.getFromState());
	}

}
