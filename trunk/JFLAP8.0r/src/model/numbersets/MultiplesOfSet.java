package model.numbersets;

import java.util.ArrayList;
import java.util.Collection;

public class MultiplesOfSet extends PredefinedSet {
	
	private int myFactor;
	private ArrayList<Integer> myValues;
	
	public MultiplesOfSet (int factor) {
		myFactor = factor;
		myValues = new ArrayList<Integer>();
	}

	@Override
	public Collection<Integer> getNumbersInSet() {
		return myValues;
	}

	@Override
	public int getNthElement(int n) {
		return n * myFactor;
	}

	@Override
	public boolean contains(int n) {
		return n % myFactor == 0;
	}

	@Override
	public String getName() {
		return "Multiples of " + myFactor;
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
	public Collection<Integer> generateNextNumbers(int n) {
		int buffer = myValues.size();
		for (int i = buffer; i < n + buffer; i++) {
			myValues.add(i * myFactor);
		}

		return myValues;
	}

	@Override
	public Collection<Integer> getValuesInRange(int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}

}
