package model.numbersets.controller;

import model.numbersets.defined.PredefinedSet;

public class PredefinedSetIterator {

	private PredefinedSet mySet; 
	
	public PredefinedSetIterator (PredefinedSet set) {
		mySet = set;
		
		mySet.extend(1000);
	}
	
	public int getNextNumber() {
		
		return 0;
	}
}
