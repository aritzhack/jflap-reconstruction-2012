package model.numbersets.operations;

import java.util.ArrayList;

import model.numbersets.AbstractNumberSet;


public abstract class SetOperation {
	
	protected ArrayList<AbstractNumberSet> myOperands;
	
	public SetOperation (ArrayList<AbstractNumberSet> operands) {
		myOperands = operands;
	}
	

	public SetOperation (AbstractNumberSet... sets) {
		myOperands = new ArrayList<AbstractNumberSet>();
		for (AbstractNumberSet s : sets) {
			myOperands.add(s);
		}
	}
	
	public abstract int getNumberOfOperands();
	
	public abstract String getName();
	
	
	public String getDescription () {
		return String.format("The %s of %s", getName(), setsToString());
	}
	
	
	public String setsToString () {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < myOperands.size() - 1; i++) {
			s.append(myOperands.get(i).getName() + ", ");
		}
		s.append(" and " + myOperands.get(myOperands.size() - 1));
		return s.toString();
	}
	
	
	public abstract AbstractNumberSet evaluate();
		
	
	
	

}
