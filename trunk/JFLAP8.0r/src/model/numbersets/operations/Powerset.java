package model.numbersets.operations;

import java.util.ArrayList;

import model.numbersets.AbstractNumberSet;


public class Powerset extends SetOperation {
	
	public Powerset (ArrayList<AbstractNumberSet> operand) {
		super(operand);
	}

	@Override
	public int getNumberOfOperands() {
		return 1;
	}

	@Override
	public String getName() {
		return "Powerset";
	}

	@Override
	public AbstractNumberSet evaluate() {
		// TODO Auto-generated method stub
		return null;
	}
	
}