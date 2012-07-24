package model.sets.elements;

/**
 * Used to generate "finite" elements for numerical sets
 * Note: generating individual or small subsets of values
 * is left to the PredefinedNumberSet subclass
 *
 */
public abstract class Generator {

	public abstract int generateNextValue() throws Exception;
	
	public abstract String getName();
	
	protected boolean checkNotOverflow (int n) {
		return Integer.toBinaryString(n).length() <= 31;
	}
}
