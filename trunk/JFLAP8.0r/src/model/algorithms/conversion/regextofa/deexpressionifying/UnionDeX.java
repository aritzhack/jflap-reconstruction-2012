package model.algorithms.conversion.regextofa.deexpressionifying;

import java.util.ArrayList;
import java.util.List;

import debug.JFLAPDebug;

import model.automata.State;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.formaldef.components.symbols.SymbolString;
import model.regex.OperatorAlphabet;
import model.regex.operators.CloseGroup;
import model.regex.operators.OpenGroup;
import model.regex.operators.UnionOperator;

public class UnionDeX extends FourStateDeX{
	
	private UnionOperator myUnionOperator;
	
	public UnionDeX(OperatorAlphabet alph) {
		super(alph);
		myUnionOperator = alph.getUnionOperator();

	}



	@Override
	protected int getShiftFromFirstOp() {
		return 1;
	}

	@Override
	protected List<FiniteStateTransition> createLambdaTransitions(
			State[] s, FiniteStateTransition trans) {
		ArrayList<FiniteStateTransition> toAdd = new ArrayList<FiniteStateTransition>();

		toAdd.add(new FiniteStateTransition(trans.getFromState(), s[0]));
		toAdd.add(new FiniteStateTransition(trans.getFromState(), s[2]));
		toAdd.add(new FiniteStateTransition(s[1], trans.getToState()));
		toAdd.add(new FiniteStateTransition(s[3], trans.getToState()));

		return toAdd;
	}



	@Override
	protected boolean isApplicable(SymbolString first, SymbolString rest) {
		return rest.startsWith(myUnionOperator);
	}

}
