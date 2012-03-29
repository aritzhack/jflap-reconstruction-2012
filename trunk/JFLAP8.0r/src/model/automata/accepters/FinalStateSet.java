package model.automata.accepters;

import errors.BooleanWrapper;
import model.automata.StateSet;

public class FinalStateSet extends StateSet {

	@Override
	public String getDescriptionName() {
		return "Final States";
	}

	@Override
	public String getDescription() {
		return "The set of final states.";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'F';
	}

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(!isEmpty(), 
				"Your Automaton requires a " + this.getDescriptionName());
	}

	
	
}
