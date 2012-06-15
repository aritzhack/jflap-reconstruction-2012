package model.numbersets;

import java.util.Collection;

import model.numbersets.parse.FormulaParser;

public abstract class FunctionSet extends PredefinedSet {
	

	
	

	@Override
	public Collection<Integer> getNumbersInSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Integer> generateNextNumbers(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNthElement(int n) {
		return evaluate(n);
	}
	
	
	public abstract int evaluate (int n);
	

	@Override
	public boolean contains(int n) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Integer> getValuesInRange(int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}
}
