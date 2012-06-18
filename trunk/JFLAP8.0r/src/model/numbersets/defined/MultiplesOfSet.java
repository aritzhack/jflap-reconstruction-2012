package model.numbersets.defined;

import java.util.Set;
import java.util.TreeSet;


public class MultiplesOfSet extends PredefinedSet {
	
	private int myFactor;
	private Set<Integer> myValues;
	
	public MultiplesOfSet (int factor) {
		myFactor = factor;
		myValues = new TreeSet<Integer>();
	}

	@Override
	public Set<Integer> getSet() {
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
	public Set<Integer> generateNextNumbers(int n) {
		int buffer = myValues.size();
		for (int i = buffer; i < n + buffer; i++) {
			myValues.add(i * myFactor);
		}

		return myValues;
	}

	@Override
	public Set<Integer> getValuesInRange(int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
