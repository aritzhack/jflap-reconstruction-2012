package model.numbersets.controller;

import java.util.ArrayList;
import java.util.Iterator;

import model.numbersets.defined.PredefinedSet;

public abstract class PredefinedSetController {
	
	PredefinedSet set;
	Iterator iter;
	
	public PredefinedSetController (PredefinedSet set) {
		this.set = set;
		
		iter = new ArrayList<Integer>().iterator();
	}
	
	
	public void showNextElement() {
		
	}

}
