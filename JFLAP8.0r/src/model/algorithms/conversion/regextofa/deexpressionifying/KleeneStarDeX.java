package model.algorithms.conversion.regextofa.deexpressionifying;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import debug.JFLAPDebug;

import model.algorithms.conversion.regextofa.DeExpressionifier;
import model.algorithms.conversion.regextofa.GeneralizedTransitionGraph;
import model.automata.State;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.formaldef.components.symbols.SymbolString;
import model.regex.OperatorAlphabet;
import model.regex.operators.KleeneStar;

public class KleeneStarDeX extends DeExpressionifier {

	private KleeneStar myKleeneStar;

	public KleeneStarDeX(OperatorAlphabet alph) {
		super(alph);
		myKleeneStar = alph.getKleeneStar();
	}

	@Override
	public List<FiniteStateTransition> adjustTransitionSet(
			FiniteStateTransition trans, GeneralizedTransitionGraph gtg) {
		SymbolString input = trans.getInput();
		trans.setInput(input.subList(0,input.size()-1));
		List<FiniteStateTransition> toAdd = new ArrayList<FiniteStateTransition>();
		//do forward lambda transition
		toAdd.add(new FiniteStateTransition(trans.getFromState(), 
												trans.getToState()));
		//do backward lambdaTransition
		toAdd.add(new FiniteStateTransition(trans.getToState(), 
											trans.getFromState()));
		
		return toAdd;
	}

	@Override
	protected boolean isApplicable(SymbolString first, SymbolString rest) {
		return rest.isEmpty() && first.endsWith(myKleeneStar);
	}



}
