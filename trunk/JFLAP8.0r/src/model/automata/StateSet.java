package model.automata;

import java.util.Iterator;
import java.util.TreeSet;

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

	@Override
	public StateSet clone() {
		StateSet clone = new StateSet();
		return new StateSet();
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
	
}
