package model.sets.operations;

import model.sets.AbstractSet;

public class CartesianProduct extends SetOperation {

	@Override
	public AbstractSet evaluate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfOperands() {
		return 2;
	}

	@Override
	public String getName() {
		return "Cartesian Product";
	}

	@Override
	public String getDescription() {
		return "The Cartesian product of " + myOperands.get(0).getName() + " and " + myOperands.get(1).getName();
	}

}
