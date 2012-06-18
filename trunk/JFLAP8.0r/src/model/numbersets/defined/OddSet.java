package model.numbersets.defined;

/**
 * @author Peggy Li
 */

import java.util.Set;
import java.util.TreeSet;


public class OddSet extends PredefinedSet {

	private Set<Integer> myValues;

	public OddSet () {
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
		return "The set of non-negative odd numbers: 1, 3, 5, 7...";
	}

	@Override
	public void reset() {
		myValues.clear();
	}


	@Override
	public Set<Integer> getValuesInRange(int min, int max) {
		if (min > max)
			return null;
		
		Set<Integer> odds = new TreeSet<Integer>();
		
		if (min % 2 == 0)	
			min++;
		for (int i = min; i <= max; i += 2) {
			odds.add(i);
		}
		
//		int start = (int) Math.floor(min/2);
//		int end = (int) Math.floor(max/2);
//		for (int i = start; i <= end; i++) {
//			odds.add(i * 2 + 1);
//		} 
		return odds;
	}


	@Override
	public int getSize() {
		return myValues.size();
	}

}
