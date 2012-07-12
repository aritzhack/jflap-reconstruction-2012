package model.sets;

import view.sets.SetView;
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

	private SetView myView;

	public SetsManager () {
		ACTIVE_REGISTRY = new ActiveSetsRegistry();
	}
	
	public void setView (SetView view) {
		myView = view;
		ACTIVE_REGISTRY.setObserver(view.getActiveSetDisplay());
	}
}
