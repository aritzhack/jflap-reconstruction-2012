package model.numbersets.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.numbersets.AbstractNumberSet;


/**
 * Maintain a collection of the active sets 
 * @author peggyli
 *
 */

public class ActiveSetsRegistry {

	private ArrayList<AbstractNumberSet> myActiveSets;
	
	public ActiveSetsRegistry () {
		myActiveSets = new ArrayList<AbstractNumberSet>();
	}
	
	/**
	 * Removes a set from the registry
	 * e.g. when user removes an existing set
	 * @param s
	 */
	public void remove (AbstractNumberSet s) {
		myActiveSets.remove(s);
	}
	
	/**
	 * Adds the set to the registry
	 * e.g. when user creates or selects a new set
	 * @param s
	 */
	public void add (AbstractNumberSet s) {
		myActiveSets.add(s);
	}
	
	/**
	 * Return all sets in registry as unmodifiable copy
	 * so that only add and remove can be used to modify
	 * 
	 * @return
	 */
	public List<AbstractNumberSet> getAll () {
		return Collections.unmodifiableList(myActiveSets);
	}
}
