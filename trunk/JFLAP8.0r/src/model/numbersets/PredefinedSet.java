package model.numbersets;

import java.util.Collection;

public abstract class PredefinedSet {

	/**
	 * Returns the numbers currently in the set
	 * 
	 * @return
	 */
	public abstract Collection<Integer> getNumbersInSet();

	/**
	 * Generate <code>n</code> numbers and adds them to the set, then returns
	 * the set of numbers with the new elements added
	 * 
	 * If the set already contains at least one element, the next <code>n</code>
	 * elements are generated and these additional elements are added to the
	 * existing set.
	 * 
	 * @param n
	 *            number of elements to add
	 * @return set with n elements added
	 */
	public abstract Collection<Integer> generateNextNumbers(int n);

	/**
	 * Returns the nth element of an ordered set
	 * 
	 * @param n
	 * @return
	 */
	public abstract int getNthElement(int n);

	/**
	 * Returns whether the number <code>n</code> belongs to the set
	 * 
	 * @param n
	 *            number being checked
	 * @return true if n is in the set
	 */
	public abstract boolean contains(int n);
	
	
	public abstract Collection<Integer> getValuesInRange (int min, int max);
	

	public abstract String getName();

	public abstract String getDescription();

	public abstract void reset();


}
