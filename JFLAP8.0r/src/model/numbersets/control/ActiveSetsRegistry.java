package model.numbersets.control;

import java.awt.Color;

import debug.JFLAPDebug;

import model.numbersets.AbstractNumberSet;
import view.numsets.ActiveSetDisplay;


/**
 * Maintain a collection of the sets the user has 
 * created or decided to add from pre-existing options
 * 
 * @author peggyli
 * 
 */

public class ActiveSetsRegistry extends SetRegistry {

	public ActiveSetsRegistry () {
		super();
	}
	
	@Override
	public void add (AbstractNumberSet a) {
		super.add(a);
		notifyObserver();
		
	}
	
	@Override
	public void remove (AbstractNumberSet a) {
		super.remove(a);
		notifyObserver();
	}
	
	
	private ActiveSetDisplay myObserver;
	
	public void setObserver (ActiveSetDisplay disp) {
		myObserver = disp;
		
	}
	
	public void notifyObserver () {
		myObserver.update(this.getArray());
	}

}
