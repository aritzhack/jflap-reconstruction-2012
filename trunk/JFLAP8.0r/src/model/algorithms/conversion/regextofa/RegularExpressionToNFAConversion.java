package model.algorithms.conversion.regextofa;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.algorithms.SteppableAlgorithm;
import model.algorithms.conversion.regextofa.deexpressionifying.ConcatDeX;
import model.algorithms.conversion.regextofa.deexpressionifying.GroupingDeX;
import model.algorithms.conversion.regextofa.deexpressionifying.KleeneStarDeX;
import model.algorithms.conversion.regextofa.deexpressionifying.UnionDeX;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.formaldef.components.symbols.Symbol;
import model.regex.OperatorAlphabet;
import model.regex.RegularExpression;

public class RegularExpressionToNFAConversion extends SteppableAlgorithm {

	private RegularExpression myRegEx;
	private GeneralizedTransitionGraph myGTG;
	private List<FiniteStateTransition> myExpressionTransitions;
	private List<FiniteStateTransition> myRemainingLambaTransitions;
	private List<DeExpressionifier> myDeExpressionifiers;

	public RegularExpressionToNFAConversion(RegularExpression re) {
		myRegEx = re;
		initDeExpressionifiers();
		reset();
	}
	
	private void initDeExpressionifiers() {
		myDeExpressionifiers = new ArrayList<DeExpressionifier>();
		OperatorAlphabet ops = myRegEx.getOperators();
		myDeExpressionifiers.add(new KleeneStarDeX(ops));
		myDeExpressionifiers.add(new GroupingDeX(ops));
		myDeExpressionifiers.add(new UnionDeX(ops));
		myDeExpressionifiers.add(new ConcatDeX(ops));

	}

	@Override
	public String getDescriptionName() {
		return "RE to NFA converter";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{new DeExpressionifyStep()};
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myGTG = new GeneralizedTransitionGraph(myRegEx);
		myRemainingLambaTransitions = new ArrayList<FiniteStateTransition>();
		updateExpressionTransitions();
		return true;
	}
	
	private void updateExpressionTransitions() {
		myExpressionTransitions = new ArrayList<FiniteStateTransition>();
		for (FiniteStateTransition t: myGTG.getTransitions()){
			if (isExpressionTransition(t))
				myExpressionTransitions.add(t);
		}
	}

	private boolean isExpressionTransition(FiniteStateTransition t) {
		return t.getInput().containsAny(myRegEx.getOperators().toArray(new Symbol[0]));
	}

	
	public void addLambdaTransition(State from, State to){
		for(FiniteStateTransition trans: myRemainingLambaTransitions){
			if (trans.getFromState().equals(from) &&
					trans.getToState().equals(to)){
				myGTG.getTransitions().add(trans);
				myRemainingLambaTransitions.remove(trans);
				return;
			}
		}
		
		throw new AlgorithmException("A lambda transition is not needed between " +
				from + " and " + to);
	}
	
	public void addAllRemainingLambdaTransitions() {
		myGTG.getTransitions().addAll(myRemainingLambaTransitions);
		myRemainingLambaTransitions.clear();
	}

	public void beginDeExpressionify(FiniteStateTransition t) {
		checkCanBeginDeExpressionify(t);
		
		for (DeExpressionifier dex: myDeExpressionifiers){
			if (dex.isApplicable(t)){
				myRemainingLambaTransitions.addAll(dex.adjustTransitionSet(t, myGTG));
				updateExpressionTransitions();
				return;
			}
		}

		throw new AlgorithmException("Unable to dexpressionify anything.");
		
	}

	private void checkCanBeginDeExpressionify(FiniteStateTransition t) {
		if (this.isDeExpressingifying())
			throw new AlgorithmException("You are already de-Expressionizing an expression.");
		else if (!myExpressionTransitions.contains(t)){
			throw new AlgorithmException("You may not de-expressionify a transition " +
					"that is not a part of the generalized transition graph");
		}
	}


	private boolean isDeExpressingifying() {
		return !myRemainingLambaTransitions.isEmpty();
	}

	public List<FiniteStateTransition> getExpressionTransitions() {
		return myExpressionTransitions;
	}
	
	public FiniteStateAcceptor getCompletedNFA(){
		if (this.isRunning())
			throw new AlgorithmException("You may not retrieve the NFA until all " +
					"transitions in the GTG have been de-expressionified.");
		
		return myGTG.createNFAFromGTG();
	}

	
	////// Algorithm Steps //////
	
	private class DeExpressionifyStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "DeExpressionify";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {

			if (isDeExpressingifying()){
				addAllRemainingLambdaTransitions();
			}
			else{
				if (getExpressionTransitions().isEmpty()) return false;
				FiniteStateTransition t = getExpressionTransitions().get(0);
				beginDeExpressionify(t);
			}
			
			return true;
		}

		@Override
		public boolean isComplete() {
			return getExpressionTransitions().isEmpty() && !isDeExpressingifying();
		}
		
	}

}
