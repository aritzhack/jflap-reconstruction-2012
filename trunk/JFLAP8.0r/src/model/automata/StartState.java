package model.automata;

import java.awt.Point;

import errors.BooleanWrapper;
import model.formaldef.components.FormalDefinitionComponent;

public class StartState extends State implements FormalDefinitionComponent {

	public StartState(String name, int id) {
		super(name, id);
	}

	public StartState() {
		this(null, -1);
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
		return new BooleanWrapper(this.getID() >= 0,
									"The Automaton requires a Start State.");
	}

	@Override
	public StartState clone() {
		return (StartState) super.clone();
	}

	@Override
	public String getDescriptionName() {
		return "Start State";
	}
	
	
	

	public void setTo(State start){
		this.setName(start.getName());
		this.setID(start.getID());
	}
	
	
	public void clear(){
		this.setName(null);
		this.setID(-1);
		
	}
	
	@Override
	public String toString() {
		return (this.getID() == -1 ? "" : this.getName());
	}
}
