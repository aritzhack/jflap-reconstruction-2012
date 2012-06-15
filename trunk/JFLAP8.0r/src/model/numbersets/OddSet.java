package model.numbersets;

import java.util.ArrayList;
import java.util.Collection;

public class OddSet extends PredefinedSet {

	private ArrayList<Integer> myValues;

	public OddSet () {
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
			myValues.add(2*i + 1);
		}
		return myValues;
	}

	@Override
	public int getNthElement(int n) {
		return 2*n - 1;
	}

	@Override
	public boolean contains(int n) {
		return n % 2 == 1;
	}

	@Override
	public String getName() {
		return "Odd Numbers";
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
