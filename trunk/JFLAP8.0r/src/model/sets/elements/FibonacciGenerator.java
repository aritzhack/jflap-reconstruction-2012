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

	/**
	 * Continue to generate next element (overflow not reached)
	 */
	private boolean doContinue;
	
	private LinkedHashSet<Integer> myValues;
	
	public FibonacciGenerator () {
		last = 0;
		previous = 1;
		doContinue = true;
		myValues = new LinkedHashSet<Integer>();
	}
	
	@Override
	public int generateNextValue() throws Exception {	
		if (!doContinue) {
			
		}
		
		if (myValues.size() == 0) 
			return last;
		else if (myValues.size() == 1) 
			return previous;
		
		current = previous + last;
		previous = current;
		last = previous;
		
		if (!checkNotOverflow(current)) 
			return current;
		doContinue = false;
		throw new Exception("Overflow");
	}

	@Override
	public String getName() {
		return "Fibonacci Generator";
	}

	
	
}
