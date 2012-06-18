package model.numbersets.defined;

/**
 * @author Peggy Li
 */


import java.util.Set;
import java.util.TreeSet;


public class EvenSet extends PredefinedSet {
	
	private Set<Integer> myValues;
	
	public EvenSet () {
		myValues = new TreeSet<Integer>();
	}
	

	@Override
	public Set<Integer> getSet() {
		return myValues;
	}

	@Override
	public Set<Integer> generateNextNumbers(int n) {
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
		return "The set of non-negative even integers: 0, 2, 4, 6...";
	}

	@Override
	public void reset() {
		myValues.clear();
	}


	@Override
	public Set<Integer> getValuesInRange(int min, int max) {
		if (min > max)
			return null;
		
		Set<Integer> evens = new TreeSet<Integer>();
		int start = (int) Math.ceil(min/2);
		int end = (int) Math.floor(max/2);
		for (int i = start; i <= end; i++) {
			evens.add(i * 2);
		}
		return evens;
	}


	@Override
	public int getSize() {
		return myValues.size();
	}

	

}
