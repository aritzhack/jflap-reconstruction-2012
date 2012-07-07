package model.numbersets.controller;

import model.numbersets.control.ActiveSetsRegistry;
import model.numbersets.gui.SetWindow;

public class SetEnvironment {
	
	/**
	 * Singleton to ensure only one set environment is open at a time
	 */
	private static SetEnvironment enviro;;
	
	public static SetEnvironment getInstance () {
		if (enviro == null) {
			enviro = new SetEnvironment();
		}
		return enviro;
	}
	
	
	private SetWindow window;
	
	private ActiveSetsRegistry active;
	
	private SetEnvironment() {
		active = new ActiveSetsRegistry();
		
		window = SetWindow.getInstance();
		
	}
	
	
	public ActiveSetsRegistry getActiveRegistry () {
		return active;
	}
	
	
	public SetWindow getWindow () {
		return window;
	}

}
