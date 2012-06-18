package model.numbersets;

import model.numbersets.defined.PredefinedSet;
import model.numbersets.function.ExponentialFunction;
import model.numbersets.function.FunctionSet;


public class Main {
	
	public static void main (String[] args) {
	
//		CustomSet custom = new CustomSet();
//		System.out.println(custom + "\n\n");
//		
//		custom.setName("Test set");
//		custom.addDescription("Random integers");
//		custom.addAll(5, 9, 0, 11, 15, 2, 3, 2, 2, 2, 6);
//		
//		System.out.println(custom + "\n\n");
//		
//		custom.remove(8);
//		System.out.println(custom + "\n\n");
//		
//		custom.remove(2);
//		System.out.println(custom + "\n\n");
		
		
		PredefinedSet set = new FunctionSet(new ExponentialFunction(4));
		System.out.println(set.contains(1024));
		
		

	}

}
