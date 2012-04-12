package model.automata;

import java.awt.Point;

import errors.BooleanWrapper;
import model.formaldef.components.FormalDefinitionComponent;

public class StartState extends FormalDefinitionComponent {

	private State myState;

	public StartState(String name, int id) {
		this(new State(name, id));
	}

	
	
	public StartState() {
		this(null);
	}

	public StartState(State state) {
		this.setTo(state);
	}



	@Override
	public String getDescription() {
		return "The Start State of the automaton.";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'S';
	}

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(myState != null,
									"The Automaton requires a Start State.");
	}

	
	@Override
	public String getDescriptionName() {
		return "Start State";
	}
	
	
	public void setTo(State start){
		myState = start;
		this.distributeChange(START_STATE_CHANGED, myState);
	}
	
	
	public void clear(){
		this.setTo(null);
	}
	
	@Override
	public String toString() {
		return (this.isComplete().isTrue() ? myState.getName() : "");
	}



	@Override
	public StartState copy() {
		return new StartState(myState.copy());
	}



	public State toStateObject() {
		return myState;
	}
}
