package model.algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import util.JFLAPConstants;

import debug.JFLAPDebug;

import model.formaldef.Describable;

public abstract class SteppableAlgorithm implements Describable,
													Steppable,
													JFLAPConstants {

	
	private AlgorithmStep[] mySteps;
	private List<SteppableAlgorithmListener> myListeners;

	
	public SteppableAlgorithm() {
		mySteps = initializeAllSteps();
		myListeners = new ArrayList<SteppableAlgorithmListener>();
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
	@Override
	public AlgorithmStep step() throws AlgorithmException{

		AlgorithmStep current = getCurrentStep();
		if (current != null){
			current.execute();
			distributeStep(current);
		}
		return current;
	}

	public boolean addListener(SteppableAlgorithmListener l){
		return myListeners.add(l);
	}
	
	public boolean removeListener(SteppableAlgorithmListener l){
		return myListeners.remove(l);
	}
	
	public void clearListeners(){
		myListeners.clear();
	}

	private void distributeStep(AlgorithmStep current) {
		for (SteppableAlgorithmListener l: myListeners){
			l.stepOccurred(current);
		}
	}


	public AlgorithmStep getCurrentStep() {
		for (AlgorithmStep step : mySteps){
			if (step.isComplete()) continue;
			return step;
		}
		return null;
	}
	
	public boolean stepToCompletion(){
		while (this.step() != null);
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
