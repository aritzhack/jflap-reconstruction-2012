package model.numbersets.function;


/**
 * a^n where a = constant, n = variable
 * 
 * @author Peggy Li
 *
 */


public class ExponentialFunction extends Function {

	private int myBase;
	
	public ExponentialFunction(int constant) {
		myBase = constant;
	}

	@Override
	public int evaluate(int n) {
		return (int) Math.pow(myBase, n);
	}
	
	
	public String toString () {
		return String.format("%d^n", myBase);
	}

	@Override
	public boolean canDerive(int y) {
		return (int) (Math.log(y)/Math.log(myBase)) == Math.log(y)/Math.log(myBase);
	}
}
