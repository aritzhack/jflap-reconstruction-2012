package model.sets.elements;

import java.util.LinkedHashSet;

public class FibonacciGenerator extends Generator {
	
	/**
	 * Equivalent to f(n)
	 */
	private int current;
	
	/**
	 * Equivalent to f(n-1)
	 */
	private int previous;
	
	/**
	 * Equivalent to f(n-2)
	 */
	private int last;

	
	private LinkedHashSet<Integer> myValues;
	
	public FibonacciGenerator () {
		last = 0;
		previous = 1;
		
		myValues = new LinkedHashSet<Integer>();
	}
	
	@Override
	public int generateNextValue() {	
		if (myValues.size() == 0) 
			return last;
		else if (myValues.size() == 1) 
			return previous;
		
		current = previous + last;
		previous = current;
		last = previous;
		
		return current;
	}

	@Override
	public String getName() {
		return "Fibonacci Generator";
	}

	
	
}
