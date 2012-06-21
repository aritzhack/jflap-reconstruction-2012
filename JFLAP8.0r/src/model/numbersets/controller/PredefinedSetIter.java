package model.numbersets.controller;

import java.util.Iterator;
import java.util.Set;

import model.numbersets.defined.PredefinedSet;

public class PredefinedSetIter {
	
	Set<Integer> set;
	
	int currentPos;
	
	public PredefinedSetIter (PredefinedSet set) {
		this.set = set.getSet();
		
		currentPos = 0;
	}
	

	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	public Object next() {
		// TODO Auto-generated method stub
		return null;
	}

	
	private void update() {
		
	}

}
