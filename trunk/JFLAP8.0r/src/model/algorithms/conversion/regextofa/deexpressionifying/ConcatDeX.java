package model.algorithms.conversion.regextofa.deexpressionifying;

import java.util.ArrayList;
import java.util.List;

import model.automata.State;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.formaldef.components.symbols.SymbolString;
import model.regex.OperatorAlphabet;
import model.regex.operators.CloseGroup;
import model.regex.operators.OpenGroup;
import model.regex.operators.UnionOperator;

public class ConcatDeX extends FourStateDeX{

	private UnionOperator myUnionOperator;
	
	public ConcatDeX(OperatorAlphabet alph) {
		super(alph);
		myUnionOperator = alph.getUnionOperator();
	}

	@Override
	protected int getShiftFromFirstOp() {
		return 0;
	}



	@Override
	protected List<FiniteStateTransition> createLambdaTransitions(
			State[] s, FiniteStateTransition trans) {
		ArrayList<FiniteStateTransition> toAdd = new ArrayList<FiniteStateTransition>();

		toAdd.add(new FiniteStateTransition(trans.getFromState(), s[0]));
		toAdd.add(new FiniteStateTransition(s[1], s[2]));
		toAdd.add(new FiniteStateTransition(s[3], trans.getToState()));

		return toAdd;
	}





	@Override
	protected boolean isApplicable(SymbolString first, SymbolString rest) {
		return rest.size() > 0 && !rest.startsWith(myUnionOperator);
	}
}
