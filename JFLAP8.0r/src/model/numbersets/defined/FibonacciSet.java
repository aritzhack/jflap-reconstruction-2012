package model.numbersets.defined;

/**
 * @author Peggy Li
 */

import java.util.Set;
import java.util.TreeSet;


public class FibonacciSet extends PredefinedSet {
	
	/**
	 * The 48th element will cause integer overflow in the form of adding
	 * a negative integer to the set. 
	 */
	private static final int MAX_SIZE = 46;

	/**
	 * f(n)
	 */
	private int current;
	/**
	 * f(n-1)
	 */
	private int prevFirst;
	/**
	 * f(n-2)
	 */
	private int prevLast;

	private Set<Integer> myValues;

	public FibonacciSet() {
		prevLast = 0;
		prevFirst = 1;

		myValues = new TreeSet<Integer>();
		generateNextNumbers(MAX_SIZE);
	}

	@Override
	public Set<Integer> generateNextNumbers(int n) {
		
		
		for (int i = 0; i <= n; i++) {
			
			if (myValues.size() == 0 && n >= 1)
				myValues.add(prevLast);
			else if (myValues.size() == 1 && n >= 2)
				myValues.add(prevFirst);
			
			else {
				current = prevFirst + prevLast;
				
				myValues.add(current);

				prevLast = prevFirst;
				prevFirst = current;
			}
		}

		return copy(myValues);
	}

	@Override
	public String getName() {
		return "Fibonacci Numbers";
	}

	@Override
	public String getDescription() {
		return "The numbers in the Fibonacci sequence where each number is "
				+ "the sum of the two previous numbers, defined recursively as"
				+ "f(0) = 0, f(1) = 1, and f(n) = f(n-1) + f(n-2) for n > 1";

	}

	@Override
	public int getNthElement(int n) {
		return fibonacci(n);
	}

	@Override
	public boolean contains(int n) {
		int a = (int) (5 * Math.pow(n, 2) + 4);
		int b = (int) (5 * Math.pow(n, 2) - 4);
		return (Math.sqrt(a) == (int) (Math.sqrt(a)) || 
				Math.sqrt(b) == (int) (Math.sqrt(b)));
	}

	@Override
	public void reset() {
		myValues.clear();

		prevFirst = 1;
		prevLast = 0;
	}

	private int fibonacci(int n) {
		if (n <= 1)
			return n;
		return fibonacci(n - 1) + fibonacci(n - 2);
	}

	@Override
	public int getSize() {
		return myValues.size();
	}

	@Override
	public Set<Integer> getSet() {
		return copy(myValues);
	}

	@Override
	public Set<Integer> getValuesInRange(int min, int max) {
		if (min < 0)	
			min = 0;
		if (max > Integer.MAX_VALUE)
			max = Integer.MAX_VALUE;
		
		Set<Integer> range = new TreeSet<Integer>();
		for (int i : myValues) {
			if (i > min && i < max) {
				range.add(i);
			}
			if (i > max)	break;
		}
		
		return range;
	}

}
