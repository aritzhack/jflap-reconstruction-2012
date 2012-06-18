package model.numbersets.function;

public abstract class Function {

	/**
	 * Returns the result that is computed when the expression is evaluated for
	 * the specified value of n
	 * 
	 * @param n
	 *            value of the variable
	 * @return expression evaluated for <code>n</code>
	 */
	public abstract int evaluate(int n);
	
	
	/**
	 * Returns whether <code>y</code> is in the range of the function
	 * @param y
	 * @return
	 */
	public abstract boolean canDerive(int y);

}
