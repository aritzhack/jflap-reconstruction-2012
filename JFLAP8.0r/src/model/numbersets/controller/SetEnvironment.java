package model.numbersets.controller;

public class SetEnvironment {
	
	/**
	 * Singleton to ensure only one set environment is open at a time
	 */
	private static SetEnvironment enviro;;
	
	public static SetEnvironment getSetEnvironment () {
		if (enviro == null) {
			enviro = new SetEnvironment();
		}
		return enviro;
	}
	
	
	ActiveSetsRegistry active;
	
	public SetEnvironment() {
		active = new ActiveSetsRegistry();
	}
	
	
	public ActiveSetsRegistry getActiveRegistry () {
		return active;
	}

}
