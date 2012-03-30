package model.automata;

import java.util.TreeSet;

import errors.BooleanWrapper;

import model.formaldef.components.FormalDefinitionComponent;

public class StateSet extends TreeSet<State> implements
		FormalDefinitionComponent {

	
	@Override
	public String getDescriptionName() {
		return "Internal States";
	}

	@Override
	public String getDescription() {
		return "The set of internal states.";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'Q';
	}

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(true);
	}

	@Override
	public StateSet clone() {
		return (StateSet) super.clone();
	}
	
	@Override
	public String toString() {
		return this.getDescriptionName() + ": " + super.toString();
	}
}
