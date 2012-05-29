package model.algorithms.conversion.regextofa.deexpressionifying;

import java.util.ArrayList;
import java.util.List;

import model.algorithms.conversion.regextofa.DeExpressionifier;
import model.automata.State;
import model.automata.TransitionSet;
import model.automata.acceptors.fsa.FSTransition;
import model.formaldef.components.symbols.SymbolString;
import model.regex.GeneralizedTransitionGraph;
import model.regex.OperatorAlphabet;

public abstract class FourStateDeX extends DeExpressionifier {

	public FourStateDeX(OperatorAlphabet alph) {
		super(alph);
	}

	@Override
	public List<FSTransition> adjustTransitionSet(
			FSTransition trans, GeneralizedTransitionGraph gtg) {
		
		SymbolString input = trans.getInput();
		SymbolString before = super.getFirstOperand(input);
		SymbolString after = input.subList(before.size()+getShiftFromFirstOp());

		TransitionSet<FSTransition> transSet = gtg.getTransitions();
		
		transSet.remove(trans);
		
		State s1 = gtg.getStates().createAndAddState();
		State s2 = gtg.getStates().createAndAddState();
		State s3 = gtg.getStates().createAndAddState();
		State s4 = gtg.getStates().createAndAddState();

		transSet.add(new FSTransition(s1, s2, before));
		transSet.add(new FSTransition(s3, s4, after));
		
		return createLambdaTransitions(new State[]{s1,s2,s3,s4},trans);
	}

	protected abstract int getShiftFromFirstOp();

	protected abstract List<FSTransition> createLambdaTransitions(State[] states,
														FSTransition trans);

}
