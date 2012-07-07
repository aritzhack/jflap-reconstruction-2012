package model.numbersets.control;

import view.numsets.SetView;
import debug.JFLAPDebug;

/**
 * Contains all information for the current set environment
 * such as the active sets and references to the GUI panes
 * 
 * @author peggyli
 *
 */

public class SetsManager {

	public static ActiveSetsRegistry ACTIVE_REGISTRY;

	public static Class[] PREDEFINED_SETS_CLASSES;

	private SetView myView;

	static {
		ACTIVE_REGISTRY = new ActiveSetsRegistry();
		PREDEFINED_SETS_CLASSES = Loader.getLoadedClasses();
	}

	public SetsManager () {
		
	}
	
	public void setView (SetView view) {
		myView = view;
		ACTIVE_REGISTRY.setObserver(view.getActiveSetDisplay());
		
	}
}
