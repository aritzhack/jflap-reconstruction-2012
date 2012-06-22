package model.numbersets.controller;

import java.util.Iterator;

import model.numbersets.AbstractNumberSet;
import model.numbersets.CustomSet;

public class SetIterator {
	
	private AbstractNumberSet mySet;
	private int myPosition;
	private Iterator iter;
	
	public SetIterator(AbstractNumberSet numberset) {
	
		iter = numberset.getSet().iterator();
		
		myPosition = 0;
	}

	
	public int getNextNumber () {
		return myPosition;
		
	}

	
//	private
	
	
	
	public static void main (String[] args) {
		CustomSet set = new CustomSet();
		set.addAll(1, 2, 3);
		
		SetIterator it = new SetIterator(set);
		while (it.iter.hasNext()) {
			System.out.println(it.iter.next());
		}
		
		set.add(4);
		
		while (it.iter.hasNext()) {
			System.out.println(it.iter.next());
		}
	}
}
