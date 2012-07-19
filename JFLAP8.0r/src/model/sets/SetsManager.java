package model.sets;

import view.sets.ActiveSetDisplay;

/**
 * Contains all information for the current set environment
 * such as the active sets and references to the GUI panes
 * 
 * @author peggyli
 *
 */

public class SetsManager {

	public static ActiveSetsRegistry ACTIVE_REGISTRY;

	public SetsManager () {
		ACTIVE_REGISTRY = new ActiveSetsRegistry();
	}
	
	public void setActiveDisplayObserver (ActiveSetDisplay disp) {
		ACTIVE_REGISTRY.setObserver(disp);
	}
}
