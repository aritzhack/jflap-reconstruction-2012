package model.automata.transducers.mealy;

import util.UtilFunctions;
import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunction;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.SymbolString;

public class MealyOutputFunction extends OutputFunction<MealyOutputFunction> {

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
	
	public boolean setInput(SymbolString s){
		return myInput.setTo(s);
	}
	
	@Override
	public boolean setTo(OutputFunction other) {
		setInput(((MealyOutputFunction) other).getInput());
		return super.setTo(other);
	}
	
	@Override
	public SymbolString[] getPartsForAlphabet(Alphabet a) {
		if (a instanceof InputAlphabet)
			return new SymbolString[]{getInput()};
		return super.getPartsForAlphabet(a);
	}
	
	@Override
	public boolean matches(FSATransition trans) {
		return this.getInput().equals(trans.getInput()) &&
				this.getState().equals(trans.getFromState());
	}

	@Override
	public int compareTo(OutputFunction o) {
		int compare = super.compareTo(o);
		if (compare != 0) return compare;
		return this.getInput().compareTo(((MealyOutputFunction) o).getInput());
	}


	@Override
	public MealyOutputFunction copy() {
		return new MealyOutputFunction(getState(), myInput, getOutput());
	}
	
	@Override
	public SymbolString[] getAllParts() {
		return UtilFunctions.combine(super.getAllParts(), myInput);
	}
	
	@Override
	protected void applySetTo(MealyOutputFunction other) {
		super.applySetTo(other);
		this.myInput.setTo(other.myInput);
	}

}
