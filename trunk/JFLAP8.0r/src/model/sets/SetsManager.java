package model.sets;

import view.sets.ActiveSetsList;

public class SetsManager {

	public static ActiveSetsRegistry ACTIVE_REGISTRY;

	public SetsManager () {
		ACTIVE_REGISTRY = new ActiveSetsRegistry();
	}
	
	public void setActiveDisplayObserver (ActiveSetsList disp) {
		ACTIVE_REGISTRY.setObserver(disp);
	}
}
