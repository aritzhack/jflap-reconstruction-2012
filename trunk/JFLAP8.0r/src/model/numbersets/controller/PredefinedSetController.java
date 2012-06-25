package model.numbersets.controller;


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


	@Override
	public AbstractNumberSet getSet() {
		return set;
	}

}
