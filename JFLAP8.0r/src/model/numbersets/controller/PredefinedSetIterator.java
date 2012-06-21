package model.numbersets.controller;

import java.util.ArrayList;
import java.util.Iterator;

import model.numbersets.defined.EvenSet;
import model.numbersets.defined.PredefinedSet;

public class PredefinedSetIterator {
	
	private static final int NUMBER_TO_GENERATE = 20;
	
	private Iterator<Integer> iter;
	private PredefinedSet set;
	
	public PredefinedSetIterator (PredefinedSet set) {
		this.set = set;
		
		iter = set.getSet().iterator();
	}
	
	
	public int getNext () {
		if (!iter.hasNext()) {
			set.generateNextNumbers(NUMBER_TO_GENERATE);
	
		}
		return iter.next();
		
	}
	
	
	
	
	public static void main (String[] args) {
		PredefinedSet set = new EvenSet();
		PredefinedSetIterator it = new PredefinedSetIterator(set);
		for (int i = 0; i < 52; i++) {
			System.out.println(it.getNext());
		}
	}

}
