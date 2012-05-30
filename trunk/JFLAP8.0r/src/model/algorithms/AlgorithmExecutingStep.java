package model.algorithms;

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
		return myAlg.stepToCompletion();
	}

	public abstract T initializeAlgorithm();

	@Override
	public boolean isComplete() {
		return myAlg != null && !myAlg.isRunning();
	}

}
