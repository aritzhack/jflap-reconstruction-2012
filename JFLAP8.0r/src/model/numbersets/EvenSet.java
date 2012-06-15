package model.numbersets;

import java.util.ArrayList;
import java.util.Collection;

public class EvenSet extends PredefinedSet {
	
	private ArrayList<Integer> myValues;
	
	public EvenSet () {
		myValues = new ArrayList<Integer>();
	}
	

	@Override
	public Collection<Integer> getNumbersInSet() {
		return myValues;
	}

	@Override
	public Collection<Integer> generateNextNumbers(int n) {
		int start = myValues.size();
		for (int i = start; i < n + start; i++) {
			myValues.add(2*i);
		}
		return myValues;
	}

	@Override
	public int getNthElement(int n) {
		return 2*n;
	}

	@Override
	public boolean contains(int n) {
		return n % 2 == 0;
	}

	@Override
	public String getName() {
		return "Even Numbers";
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
