package model.numbersets;

import java.util.ArrayList;
import java.util.Collection;

public class PrimeSet extends PredefinedSet {
	
	private ArrayList<Integer> myValues;
	
	private int myCurrent;
	
	public PrimeSet () {
		myCurrent = 1;
		myValues = new ArrayList<Integer>();
	}

	@Override
	public Collection<Integer> getNumbersInSet() {
		return myValues;
	}

	@Override
	public Collection<Integer> generateNextNumbers(int n) {
		for (int i = 0; i < n; i++) {
			myValues.add(getNextPrime());
		}
		return myValues;
	}

	@Override
	public int getNthElement(int n) {
		/*
		 * There is NOT a specific formula for the nth prime.
		 * This uses a brute force approach and run-time can
		 * be large as the value of n increases.
		 */
		
		while (myValues.size() < n-1 ) {
			myValues.add(getNextPrime());
		}
		return myValues.get(n-1);
	}

	@Override
	public boolean contains(int n) {
		if (myValues.get(myValues.size()-1) > n) {
			return myValues.contains(n);
		}
		while (myValues.get(myValues.size()-1) < n) {
			generateNextNumbers(1);
		}
		return myValues.contains(n);
	}

	@Override
	public String getName() {
		return "Prime Numbers";
	}

	@Override
	public String getDescription() {
		return "Positive integers greater than 1 whose only two factors are 1 and the number itself";
	}

	@Override
	public void reset() {
		myCurrent = 2;
	}
	
	
	private int getNextPrime () {
		while (!isPrime(myCurrent)) {
			myCurrent++;
		}
		
		int prime = myCurrent;
		myCurrent++;
		return prime;
	}
	
	private boolean isPrime (int n) {
		for (int i = 2; i <= Math.sqrt(n); i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	@Override
	public Collection<Integer> getValuesInRange(int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}

}
