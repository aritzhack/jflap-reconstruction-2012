package model.numbersets.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

import model.numbersets.AbstractNumberSet;


public abstract class SetRegistry {

	protected LinkedHashSet<AbstractNumberSet> mySetRegistry;

	public SetRegistry() {
		mySetRegistry = new LinkedHashSet<AbstractNumberSet>();
	}


	/**
	 * Removes a set from the registry
	 * 
	 * @param s
	 */
	public void remove (AbstractNumberSet a) {
		mySetRegistry.remove(a);
	}


	/**
	 * Adds the set to the registry e.g. user creates or selects a new set
	 * 
	 * @param s
	 */
	public void add (AbstractNumberSet a) {
		mySetRegistry.add(a);
	}


	/**
	 * Return string array containing the names of all sets in the list
	 * @return
	 */
	public String[] getArray () {
		String[] active = new String[mySetRegistry.size()];
//		for (int i = 0; i < mySetRegistry.size(); i++) {
////			active[i] = mySetRegistry.get(i).getName();
//		}
		int i = 0;
		Iterator<AbstractNumberSet> it = mySetRegistry.iterator();
		while (it.hasNext()) {
			active[i] = it.next().getName();
			i++;
		}
		
		return active;
	}
}
