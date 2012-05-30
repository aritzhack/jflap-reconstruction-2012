package model.algorithms;

import java.util.LinkedList;

import debug.JFLAPDebug;

import model.formaldef.Describable;
import model.util.JFLAPConstants;

public abstract class SteppableAlgorithm implements Describable, JFLAPConstants {

	
	private AlgorithmStep[] mySteps;

	
	public SteppableAlgorithm() {
		mySteps = initializeAllSteps();
	}
	
	
	/**
	 * Initialize the sequence of {@link AlgorithmStep} for this
	 * {@link SteppableAlgorithm}
	 * @return
	 */
	public abstract AlgorithmStep[] initializeAllSteps();



	/**
	 * Progresses this algorithm to the next step, and returns
	 * a boolean as to whether or not the step was successful.
	 * 
	 * @return
	 */
	public boolean step() throws AlgorithmException{

		AlgorithmStep current = getCurrentStep();
		
		if (current == null) return false;
		return current.execute();
	}


	public AlgorithmStep getCurrentStep() {
		for (AlgorithmStep step : mySteps){
			if (step.isComplete()) continue;
			return step;
		}
		return null;
	}
	
	public boolean stepToCompletion(){
		while (this.step());
		return !this.isRunning();
		
	}
	
	public boolean isRunning(){
		return this.getCurrentStep() != null;
	}
	
	/**
	 * Resets this algorithm to its original state.
	 * 
	 * @return true if the reset is successful
	 */
	public abstract boolean reset() throws AlgorithmException;
	
	
	
}
