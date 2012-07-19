package model.sets;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

import debug.JFLAPDebug;

import model.numbersets.AbstractNumberSet;
import view.sets.ActiveSetDisplay;


/**
 * Maintain a collection of the sets the user has 
 * created or decided to add from pre-existing options
 * 
 * @author peggyli
 * 
 */

public class ActiveSetsRegistry {
	
//	private LinkedHashSet<AbstractSet> myRegistry;
	
	private HashMap<String, AbstractSet> myRegistryMap;
	

	public ActiveSetsRegistry () {
//		myRegistry = new LinkedHashSet<AbstractSet>();
		
		myRegistryMap = new HashMap<String, AbstractSet>();
	}
	
	/**
	 * Adds a set to the active sets registry
	 * @param set
	 */
	public void add (AbstractSet set) {
//		myRegistry.add(set);
		myRegistryMap.put(set.getName(), set);
		
		notifyObserver();
		
	}
	
	public void remove (AbstractSet set) {
//		myRegistry.remove(set);
		myRegistryMap.remove(set.getName());
		notifyObserver();
	}
	
	
	public AbstractSet getSetByName (String key) {
		return myRegistryMap.get(key);
	}
	
	private ActiveSetDisplay myObserver;
	
	public void setObserver (ActiveSetDisplay disp) {
		myObserver = disp;
		
	}
	
	public void notifyObserver () {
		myObserver.update(this.getNameArray());
	}
	
	
	public ArrayList<AbstractSet> getSelectedSets () {
		return myObserver.getSelectedSets();
	}
	
	/**
	 * Return array of strings representing the names of the
	 * sets in the registry
	 * @return
	 */
	private String[] getNameArray () {
//		String[] names = new String[myRegistry.size()];
//		int index = 0;
//		Iterator<AbstractSet> iter = myRegistry.iterator();
//		while (iter.hasNext()) {
//			names[index] = iter.next().getName();
//			index++;
//		}
		
		String[] names = new String[myRegistryMap.size()];
		int index = 0;
		Iterator<String> iter = myRegistryMap.keySet().iterator();
		while (iter.hasNext()) {
			names[index] = iter.next();
			index++;
		}
		return names;
	}
	

}
