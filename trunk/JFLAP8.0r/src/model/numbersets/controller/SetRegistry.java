package model.numbersets.controller;

import java.util.ArrayList;

import model.numbersets.AbstractNumberSet;

public abstract class SetRegistry {

	/**
	 * Removes a set from the registry e.g. when user removes an existing set
	 * 
	 * @param s
	 */
	public abstract void remove (AbstractNumberSet a);
	
	/**
	 * Adds the set to the registry e.g. when user creates or selects a new set
	 * 
	 * @param s
	 */
	public abstract void add (AbstractNumberSet a);
	
	/**
	 * Return string array containing the names of all sets in the list
	 * @param sets 
	 * 
	 * @return
	 */
	public String[] getArray (ArrayList<AbstractNumberSet> sets) {
		String[] active = new String[sets.size()];
		for (int i = 0; i < sets.size(); i++) {
			active[i] = sets.get(i).getName();
		}
		return active;
	}
}
