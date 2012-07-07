package model.numbersets.controller;

import model.numbersets.AbstractNumberSet;
import model.numbersets.control.SetsManager;


public abstract class AbstractSetController {
	
	/**
	 * Return next element to show in order
	 * 
	 * e.g. set is [1, 2, 3, 4, 5]
	 * 		1, 2 currently displayed to user
	 * 		return 3, the next number to show
	 * 		on next call, return 4
	 * 
	 * @return
	 */
	public abstract int getNextElement();

	
	public boolean contains (String input) {
		try {
			int num = Integer.parseInt(input);
			return getSet().contains(num);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public abstract AbstractNumberSet getSet ();
}
