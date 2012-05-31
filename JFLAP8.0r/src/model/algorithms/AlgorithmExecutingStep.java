package model.algorithms;

import model.algorithms.fsa.minimizer.MinimizeDFAAlgorithm;

public abstract class AlgorithmExecutingStep<T extends SteppableAlgorithm> implements AlgorithmStep {

	private T myAlg;

	public T getAlgorithm(){
		return myAlg;
	}
	
	@Override
	public String getDescriptionName() {
		return myAlg.getDescriptionName();
	}

	@Override
	public String getDescription() {
		return myAlg.getDescription();
	}

	@Override
	public boolean execute() throws AlgorithmException {
		myAlg = initializeAlgorithm();
		boolean completed = myAlg.stepToCompletion();
		if (completed)
			updateDataInMetaAlgorithm();
		return completed;
	}

	/**	
	 * does nothing by default, can be overridden to adjust meta 
	 * data in the algorithm this class is implemented in.
	 * 
	 * @see MinimizeDFAAlgorithm
	 * 
	 */
	public void updateDataInMetaAlgorithm(){
		//does nothing by default, can be overridden to adjust meta 
		//data in the algorithm this class is implemented in;
	}

	public abstract T initializeAlgorithm();

	@Override
	public boolean isComplete() {
		return myAlg != null && !myAlg.isRunning();
	}

}
