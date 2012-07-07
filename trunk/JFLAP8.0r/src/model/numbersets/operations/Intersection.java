package model.numbersets.operations;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import model.numbersets.AbstractNumberSet;

public class Intersection extends SetOperation {
	
	public Intersection (ArrayList<AbstractNumberSet> operands) {
		super(operands);
	}

	@Override
	public int getNumberOfOperands() {
		return 2;
	}

	@Override
	public String getName() {
		return "Intersection";
	}

	@Override
	public AbstractNumberSet evaluate() {
		AbstractNumberSet op1 = myOperands.get(0);
		AbstractNumberSet op2 = myOperands.get(1);
		
		Set intersect = new TreeSet();
		
		
		return null;
	}

	
	
}
