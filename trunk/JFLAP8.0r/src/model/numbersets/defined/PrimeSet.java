package model.numbersets.defined;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


public class PrimeSet extends PredefinedSet {
	
	private Set<Integer> myValues;
	
	private int myCurrent;
	
	public PrimeSet () {
		myCurrent = 1;
		myValues = new TreeSet<Integer>();
		
		extend(DEFAULT_NUMBER_TO_ADD);
		
		System.out.println("PRIME SET INITIALIZED");
	}

	@Override
	public Set<Integer> getSet() {
		return myValues;
	}

	@Override
	public Set<Integer> extend(int n) {
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
		
		ArrayList<Integer> primes = new ArrayList<Integer>(myValues);
		while (primes.size() < n-1 ) {
			primes.add(getNextPrime());
		}
		return primes.get(n-1);
	}

	@Override
	public boolean contains(int n) {
		return isPrime(n);
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
		if (n < 2)	return false;
		
		for (int i = 2; i <= Math.sqrt(n); i++) {
			if (n % i == 0)
				return false;
		}
		return true;
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
