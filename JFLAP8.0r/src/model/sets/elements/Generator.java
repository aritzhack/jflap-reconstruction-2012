package model.sets.elements;

/**
 * Generates the next element in an infinite set
 * that follows some implementable pattern
 *
 */
public abstract class Generator {

	public abstract int generateNextValue();
	
	public abstract String getName();
	
	protected boolean checkNotOverflow (int n) {
		return Integer.toBinaryString(n).length() <= 31;
	}
}
