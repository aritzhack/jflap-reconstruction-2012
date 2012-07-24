package model.sets.num;

import java.util.Set;
import java.util.TreeSet;

import model.sets.AbstractSet;
import model.sets.CustomFiniteSet;
import model.sets.elements.Element;
import model.sets.elements.FibonacciGenerator;
import model.sets.elements.Generator;


public class FibonacciSet extends PredefinedNumberSet {


	private Generator myGenerator;
	private Set<Element> myElements;

	public FibonacciSet () {
		myGenerator = new FibonacciGenerator();
		myElements = new TreeSet<Element>();
	}

	@Override
	public void generateMore() {
		// TODO Auto-generated method stub
		myElements.add(new Element(myGenerator.generateNextValue()));
	}

	@Override
	public Set<Element> getSet() {
		return myElements;
	}


	@Override
	public String getName() {
		return "Fibonacci";
	}

	@Override
	public String getDescription() {
		return "The set of numbers in the Fibonacci sequence from 0, 1, 1...";
	}

	@Override
	public boolean contains (Element e) {
		return contains(Integer.parseInt(e.getValue()));
	}


	public boolean contains(int n) {
		int a = (int) (5 * Math.pow(n, 2) + 4);
		int b = (int) (5 * Math.pow(n, 2) - 4);
		return (Math.sqrt(a) == (int) (Math.sqrt(a)) || 
				Math.sqrt(b) == (int) (Math.sqrt(b)));
	}

	@Override
	public AbstractSet getNumbersInRange(int min, int max) {
		if (min < 0)	
			min = 0;
		if (max > Integer.MAX_VALUE) 
			max = Integer.MAX_VALUE;

		Set<Element> range = new TreeSet<Element>();
		for (Element e : myElements) {
			// if between min and max
			range.add(e);
		}

		return new CustomFiniteSet(range);

	}

	@Override
	public Element getNthElement(int n) {
		return new Element(fibonacci(n));
	}


	public int fibonacci (int n) {
		if (n <= 1) 
			return n;
		return fibonacci(n-2) + fibonacci(n-1);
	}



}
