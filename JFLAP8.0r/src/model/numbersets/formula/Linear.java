package model.numbersets.formula;

public class Linear {

	private int mySlope;
	private int myConstant;
	
	public Linear (int slope, int constant) {
		mySlope = slope;
		myConstant = constant;
	}
	
	public Linear (int slope) {
		this (slope, 0);
	}
	
	public String toString () {
		return String.format("%dn+%d", mySlope, myConstant);
	}
	
}
