package model.numbersets.controller;


import model.numbersets.defined.PredefinedSet;

public abstract class PredefinedSetController {
	
	PredefinedSet set;
	
	public PredefinedSetController (PredefinedSet set) {
		this.set = set;
		
	}
	
	
	public void showNextElement() {
		
	}

}
