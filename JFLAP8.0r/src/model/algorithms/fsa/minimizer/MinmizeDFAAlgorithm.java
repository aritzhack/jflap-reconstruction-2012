package model.algorithms.fsa.minimizer;

import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmExecutingStep;
import model.algorithms.AlgorithmStep;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.SteppableAlgorithm;
import model.algorithms.fsa.AddTrapStateAlgorithm;
import model.algorithms.fsa.InacessableStateRemover;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.formaldef.FormalDefinition;

public class MinmizeDFAAlgorithm extends FormalDefinitionAlgorithm<FiniteStateAcceptor> {

	private FiniteStateAcceptor myTemporaryDFA;
	private ConstructMinimizeTreeStep myMinimizeTreeStep;
	private AlgorithmExecutingStep<BuildMinimalDFA> myBuildMinimalDFAStep;

	public MinmizeDFAAlgorithm(FiniteStateAcceptor fd) {
		super(fd);
	}
	
	public FiniteStateAcceptor getStartingDFA(){
		return super.getOriginalDefinition();
	}

	@Override
	public String getDescriptionName() {
		return "Minimize DFA";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(FiniteStateAcceptor fd) {
		return null;
	}

	@Override
	public AlgorithmExecutingStep[] initializeAllSteps() {
		myMinimizeTreeStep = new ConstructMinimizeTreeStep();
		return new AlgorithmExecutingStep[]{
						new RemoveInacessibleStates(),
						new AddTrapStateStep(),
						myMinimizeTreeStep};
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myTemporaryDFA = (FiniteStateAcceptor) this.getStartingDFA().copy();
		return false;
	}
	
	@Override
	public AlgorithmExecutingStep getCurrentStep() {
		if (myMinimizeTreeStep.isComplete() &&
				myBuildMinimalDFAStep == null){
			MinimizeTreeModel finalTree = myMinimizeTreeStep.getAlgorithm().getMinimizeTree();
			SteppableAlgorithm alg = new BuildMinimalDFA(finalTree);
			return myBuildMinimalDFAStep = new AlgorithmExecutingStep(alg);
			
		}
		else if (myBuildMinimalDFAStep != null &&
					!myBuildMinimalDFAStep.isComplete()){
			return myBuildMinimalDFAStep;
		}
		return (AlgorithmExecutingStep) super.getCurrentStep();
	}
	
	private class RemoveInacessibleStates extends AlgorithmExecutingStep<InacessableStateRemover>{

		public RemoveInacessibleStates() {
			super(new InacessableStateRemover(myTemporaryDFA));
		}
		
	}

	private class AddTrapStateStep extends AlgorithmExecutingStep<AddTrapStateAlgorithm>{

		public AddTrapStateStep() {
			super(new AddTrapStateAlgorithm(myTemporaryDFA));
		}
		
	}

	private class ConstructMinimizeTreeStep extends AlgorithmExecutingStep<BuildMinimizeTreeAlgorithm>{
		
		public ConstructMinimizeTreeStep(){
			super(new BuildMinimizeTreeAlgorithm(myTemporaryDFA));
		}
	}
}
