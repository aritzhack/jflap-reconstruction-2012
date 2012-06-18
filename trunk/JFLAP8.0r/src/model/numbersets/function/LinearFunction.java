package model.numbersets.function;

public class LinearFunction extends Function {

	private int mySlope;
	private int myConstant;
	
	public LinearFunction (int slope, int constant) {
		mySlope = slope;
		myConstant = constant;
	}
	
	public LinearFunction (int slope) {
		this (slope, 0);
	}

	
	public String toString () {
		return String.format("%dn+%d", mySlope, myConstant);
	}

	@Override
	public int evaluate(int n) {
		return mySlope * n + myConstant;
	}
	
	/**
	 * Returns whether an integer value of <code>n</code> can
	 * yield in the expression evaluating to equal <code>y</code>
	 * 
	 * For example, if expression is 2n+5, and y=11, returns true
	 * because 2n+5=11 for n=3; returns false for y=12 though
	 * 
	 * @param y
	 * @return
	 */
	public boolean canDerive(int y) {
		return (y - myConstant) % mySlope == 0;
	}
	
}
