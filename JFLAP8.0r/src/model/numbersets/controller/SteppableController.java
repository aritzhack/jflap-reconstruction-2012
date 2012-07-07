package model.numbersets.controller;

import java.util.Set;

import model.numbersets.defined.PredefinedSet;

public class SteppableController {
	
	private PredefinedSet mySet;
	
	public SteppableController (PredefinedSet set) {
		mySet = set;
	}

	
	public int step () {
		Set<Integer> next = mySet.extend(1);
		
		
		return 0;
	}
	
	
	public PredefinedSet getSet () {
		return mySet;
	}
	
	
}
