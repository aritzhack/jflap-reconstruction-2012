package model.automata;

import java.awt.Point;

import errors.BooleanWrapper;
import model.formaldef.components.FormalDefinitionComponent;

public class StartState extends State implements FormalDefinitionComponent {

	public StartState(String name, int id, Point location) {
		super(name, id, location);
	}

	public StartState() {
		this(null, -1, null);
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
		return new BooleanWrapper(this.getID() > 0,
									"The Automaton requires a StartState.");
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
		this.setLocation(start.getLocation());
	}
	
	
	public void clear(){
		this.setName(null);
		this.setID(-1);
		this.setLocation(null);
		
	}
	
	@Override
	public String toString() {
		return getDescriptionName() + ": "+ (this.getID() == -1 ? "" : this.getName());
	}
}
