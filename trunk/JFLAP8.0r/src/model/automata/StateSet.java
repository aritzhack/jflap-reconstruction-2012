package model.automata;

import java.util.Iterator;
import java.util.TreeSet;

import universe.preferences.JFLAPPreferences;


import errors.BooleanWrapper;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.SetComponent;

public class StateSet extends SetComponent<State> {

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

	public int getNextUnusedID() {
		
		int i = 0;
		
		while (this.getStateWithID(i) != null){
			i++;
		}
		return i;
	}

	public State getStateWithID(int id) {
		for (State s: this){
			if (s.getID() == id)
				return s;
		}
		
		return null;
	}

	public State createAndAddState() {
		int id = this.getNextUnusedID();
		State s = new State(JFLAPPreferences.getDefaultStateNameBase()+id, id);
		this.add(s);
		return s;
	}
	
	@Override
	public StateSet copy() {
		return (StateSet) super.copy();
	}
	
}
