package model.sets.num;

import java.util.Iterator;
import java.util.Set;

import model.sets.AbstractSet;
import model.sets.InfiniteSet;
import model.sets.elements.Element;

/*
 * Use this class to create specific concrete infinite sets 
 * (e.g. not 'general' or abstract sets like Finite or Finite,
 * but not Customizable ones that require user input to build;
 * currently subclasses include PrimesSet, FibonacciSet, etc.)
 */

public abstract class PredefinedNumberSet extends InfiniteSet {


	public abstract AbstractSet getNumbersInRange(int min, int max);

	public abstract Element getNthElement(int n);
	
	@Override
	public String getSetAsString() {
		StringBuilder str = new StringBuilder();
		Iterator<Element> iter = getSet().iterator();
		while (iter.hasNext()) {
			str.append(iter.next() + ", ");
		}
		str.append("...");
		return str.toString();
		
	}
}
