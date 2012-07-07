package model.numbersets.controller;


import java.util.Set;

import model.numbersets.AbstractNumberSet;
import model.numbersets.defined.PredefinedSet;

public class PredefinedSetController extends AbstractSetController {
	
	PredefinedSet set;
	
	public PredefinedSetController (PredefinedSet set) {
		this.set = set;
		
	}
	
	
	public void showNextElement() {
		
	}


	@Override
	public int getNextElement() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public Set<Integer> getElementsBetween (int min, int max) {
		return set.getValuesInRange(min, max);
	}


	@Override
	public AbstractNumberSet getSet() {
		return set;
	}

}
