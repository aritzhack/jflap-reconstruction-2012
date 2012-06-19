package model.algorithms.transform.turing;

import java.util.ArrayList;
import java.util.List;

import model.algorithms.*;
import model.algorithms.transform.FormalDefinitionTransformAlgorithm;
import model.automata.State;
import model.automata.turing.*;
import model.formaldef.components.symbols.Symbol;
import errors.BooleanWrapper;

public class StayOptionRemover extends
		FormalDefinitionTransformAlgorithm<MultiTapeTuringMachine> {
	private List<MultiTapeTMTransition> stayTransitions;

	public StayOptionRemover(MultiTapeTuringMachine tm) {
		super(tm);
		initializeStayTransitions();
	}

	@Override
	public boolean reset() {
		super.reset();
		initializeStayTransitions();
		return true;
	}

	public void initializeStayTransitions() {
		stayTransitions = new ArrayList<MultiTapeTMTransition>();
		for (MultiTapeTMTransition transition : getOriginalDefinition()
				.getTransitions()) {
			if (transition.getMove(0).equals(TuringMachineMove.STAY)) {
				stayTransitions.add(transition);
			}
		}
	}

	@Override
	public String getDescriptionName() {
		return "Stay Option Remover";
	}

	@Override
	public String getDescription() {
		return "Algorithm to remove Stay transitions from Turing Machines";
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(MultiTapeTuringMachine tm) {
		if(tm.getNumTapes()!=1) return new BooleanWrapper[]{new BooleanWrapper(false, "The Turing machine has multiple tapes")};
		return new BooleanWrapper[0];
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[] { new ProductionReplacementStep() };
	}

	private class ProductionReplacementStep implements AlgorithmStep {

		@Override
		public String getDescriptionName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return replaceStayTransitions();
		}

		@Override
		public boolean isComplete() {
			return stayTransitions.isEmpty();
		}

	}

	public boolean replaceStayTransitions(){
		MultiTapeTMTransition transition = stayTransitions.remove(0);
		getTransformedDefinition().getTransitions().remove(transition);
		State newState = getTransformedDefinition().getStates().createAndAddState();
		MultiTapeTMTransition rightReplacement = 
		new MultiTapeTMTransition(transition.getFromState(), newState, 
					transition.getRead(0), transition.getWrite(0), 
					TuringMachineMove.RIGHT);
		getTransformedDefinition().getTransitions().add(rightReplacement);
		for(Symbol c : getOriginalDefinition().getTapeAlphabet()){
			MultiTapeTMTransition leftReplacement = new MultiTapeTMTransition(newState, transition.getToState(), 
					c, c, 
					TuringMachineMove.LEFT);
			getTransformedDefinition().getTransitions().add(leftReplacement);
		}
		return true;
	}
}
