package model.numbersets.function;

import java.util.Set;
import java.util.TreeSet;

import model.numbersets.defined.PredefinedSet;

public class FunctionSet extends PredefinedSet {

	private Function myFunction;
	private Set<Integer> myValues;

	private int myCurrent;

	/**
	 * 
	 * @param func
	 * @param initial
	 *            the starting value for <code>n</code> - 
	 *            if no value is provided, default is 0
	 */
	public FunctionSet(Function func, int initial) {
		myFunction = func;
		myCurrent = initial;
		
		myValues = new TreeSet<Integer>();
	}
	
	public FunctionSet(Function func) {
		this(func, 0);
	}

	@Override
	public Set<Integer> getSet() {
		return copy(myValues);
	}

	@Override
	public Set<Integer> generateNextNumbers(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNthElement(int n) {
		return myFunction.evaluate(n);
	}

	@Override
	public boolean contains(int n) {
		return myFunction.canDerive(n);
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
		myCurrent = 0;
		myValues.clear();
	}

	@Override
	public Set<Integer> getValuesInRange(int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize() {
		return myValues.size();
	}
}
