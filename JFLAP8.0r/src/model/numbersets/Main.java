package model.numbersets;

import model.numbersets.parse.FormulaParser;

public class Main {
	
	public static void main (String[] args) {
	
		
//		PredefinedSet set = new MultiplesOfSet(5);
//		
//		// print first n numbers
//		System.out.println(set.generateNextNumbers(0));		// empty
//		System.out.println(set.generateNextNumbers(1));		// first
//		System.out.println(set.generateNextNumbers(7));		// n more
//		
//		// print contains n
//		System.out.println(set.contains(7));		// true
//		System.out.println(set.contains(14));		// false
		
		
		FormulaParser parser = new FormulaParser("5n - 6 ");
		
		System.out.println(parser.getStringFromIndex(0));
		
		
	}

}
