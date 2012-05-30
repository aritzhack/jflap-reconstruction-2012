package model.algorithms;

public class AlgorithmExecutingStep<T extends SteppableAlgorithm> implements AlgorithmStep {

	private T myAlg;

	public AlgorithmExecutingStep(T alg) {
		myAlg = alg;
	}
	
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
		return myAlg.stepToCompletion();
	}

	@Override
	public boolean isComplete() {
		return !myAlg.isRunning();
	}

}
