package model.sets.operations;

import java.util.ArrayList;

import model.sets.AbstractSet;

/**
 * To add a new set operation, subclass this class and
 * implements its abstract methods. It will automatically
 * be added via reflection.
 * 
 * @author Peggy Li
 *
 */

public abstract class SetOperation {
	
	protected ArrayList<AbstractSet> myOperands;
	
	public SetOperation () { }
	
	public void setOperands (ArrayList<AbstractSet> operands) throws SetOperationException {
		if (operands.size() != getNumberOfOperands()) {
			throw new SetOperationException("Wrong # of operands!");
		}
		
		myOperands = operands;
	}
	
	
	public abstract AbstractSet evaluate();
	
	public abstract int getNumberOfOperands();
	
	public abstract String getName();
	
	public abstract String getDescription();

}
