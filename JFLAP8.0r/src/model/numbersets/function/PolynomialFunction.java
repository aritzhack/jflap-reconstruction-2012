package model.numbersets.function;

/**
 * 
 * @author Peggy Li
 *
 */

public class PolynomialFunction extends Function {
	
	private int coefficient;
	private int degree;
	
	public PolynomialFunction (int coefficient, int degree) {
		this.coefficient = coefficient;
		this.degree = degree;
	}

	@Override
	public int evaluate(int n) {
		return coefficient * (int) Math.pow(n, degree);
	}

	@Override
	public boolean canDerive(int y) {
		return Math.pow(y/coefficient, 1/degree) == 
				(int)(Math.pow(y/coefficient, 1/degree));
	}

	
	public String toString () {
		return String.format("%dn^%d", coefficient, degree);
	}
}
