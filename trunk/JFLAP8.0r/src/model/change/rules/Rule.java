package model.change.rules;

import errors.BooleanWrapper;
import model.change.ChangeEvent;
import model.change.ChangeRelated;
import model.formaldef.Describable;

public abstract class Rule extends ChangeRelated implements Describable{

	public Rule(int type) {
		super(type);
	}

	public abstract BooleanWrapper checkRule(ChangeEvent event);
	
	public String toString(){
		return this.getDescriptionName() + ": " + this.getDescription();
	}
	
}
