package model.numbersets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FibonacciSet extends PredefinedSet {

	boolean debug = false;

	private int current; // f(n)
	private int prevFirst; // f(n-1)
	private int prevLast; // f(n-2)

	private ArrayList<Integer> myValues;

	public FibonacciSet() {
		prevLast = 0;
		prevFirst = 1;

		myValues = new ArrayList<Integer>();
	}

	@Override
	public Collection<Integer> generateNextNumbers(int n) {
		for (int i = 0; i < n; i++) {

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

		return myValues;
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
		if (myValues.get(myValues.size() - 1) > n) {
			return myValues.contains(n);
		}
		while (myValues.get(myValues.size() - 1) < n) {
			generateNextNumbers(1);
		}
		return myValues.contains(n);
	}

	@Override
	public void reset() {
		prevFirst = 1;
		prevLast = 0;

	}

	private int fibonacci(int n) {
		if (n <= 1)
			return n;
		return fibonacci(n - 1) + fibonacci(n - 2);
	}

	@Override
	public Collection<Integer> getNumbersInSet() {
		ArrayList<Integer> copy = new ArrayList<Integer>();
		Collections.copy(copy, myValues);
		return copy;
	}

	@Override
	public Collection<Integer> getValuesInRange(int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}

}
