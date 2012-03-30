package model.automata.algorithms;

import java.util.LinkedList;

import model.formaldef.Describable;

public interface SteppableAlgorithm extends Describable {

	/**
	 * Progresses this algorithm to the next step, and returns
	 * a boolean as to whether or not the step was successful.
	 * 
	 * @return
	 */
	public boolean step();
	
	
	/**
	 * Resets this algorithm to its original state.
	 * 
	 * @return true if the reset is successful
	 */
	public boolean reset();
	
	
	
}
