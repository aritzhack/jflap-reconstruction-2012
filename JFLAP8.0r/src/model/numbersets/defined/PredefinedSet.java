package model.numbersets.defined;

/**
 * Abstract superclass for all predefined sets
 * 
 * @author Peggy Li
 */

import java.util.Set;
import java.util.TreeSet;

public abstract class PredefinedSet {

	/**
	 * Returns the numbers currently in the set
	 * 
	 * @return
	 */
	public abstract Set<Integer> getSet();

	/**
	 * 
	 * @return the number of elements in the set
	 */
	public abstract int getSize();

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
	public abstract Set<Integer> generateNextNumbers(int n);

	/**
	 * Returns the nth element of an ordered set with a defined initial value
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

	/**
	 * Returns all elements of the set that fall in the specified range, where
	 * both <code>min</code> and <code>max</code> are inclusive
	 * 
	 * @param min
	 *            lower
	 * @param max
	 * @return
	 */
	public abstract Set<Integer> getValuesInRange(int min, int max);

	/**
	 * Returns the name of the set
	 * 
	 * @return name of the set
	 */
	public abstract String getName();

	/**
	 * Returns a description of the set, such as a short definition
	 * 
	 * @return string describing the set
	 */
	public abstract String getDescription();

	/**
	 * Resets the state of the set by clearing all elements generated and
	 * resetting any values to the initial default values, but preserves any
	 * parameters that act as properties of the set
	 */
	public abstract void reset();

	/**
	 * Returns a copy of the set passed in as a parameter, where elements are in
	 * sorted order (same order if the parameter set was already sorted)
	 * 
	 * @param original
	 *            the set being copied
	 * @return copy of the set
	 */
	public Set<Integer> copy(Set<Integer> original) {
		Set<Integer> copy = new TreeSet<Integer>(original);
		return copy;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(getName() + "\n");
		sb.append(getSet());
		return sb.toString();
	}

}
