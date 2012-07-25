package view.sets;

import model.sets.num.CongruenceSet;
import model.sets.num.EvensSet;
import model.sets.num.MultiplesSet;
import model.sets.num.FibonacciSet;
import model.sets.num.OddsSet;
import model.sets.num.PrimesSet;

public class PredefinedNumberSets {
	
	public static final Class[] PREDEFINED_SETS = {
		FibonacciSet.class,
		PrimesSet.class,
		EvensSet.class,
		OddsSet.class,
		MultiplesSet.class,
		CongruenceSet.class
	};

	
	/**
	 * Returns an array of the names of the parameters
	 * required to instantiate the class
	 * Assumes that all parameters are of type int 
	 * 
	 * @param c the class 
	 */
	public String[] getConstructorParameters(Class c) {
		if (c.equals(MultiplesSet.class)) {
			return new String[]{"factor"};
		}
		if (c.equals(CongruenceSet.class)) {
			return new String[]{"first", "second"};
		}
		return new String[]{};
	}
	
}
