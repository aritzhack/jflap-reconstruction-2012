package model.sets.num;

import java.util.ArrayList;

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
		return "...";
		
	}
}
