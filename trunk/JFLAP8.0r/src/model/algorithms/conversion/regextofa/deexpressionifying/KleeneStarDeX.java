package model.algorithms.conversion.regextofa.deexpressionifying;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import debug.JFLAPDebug;

import model.algorithms.conversion.regextofa.DeExpressionifier;
import model.automata.State;
import model.automata.acceptors.fsa.FSTransition;
import model.formaldef.components.symbols.SymbolString;
import model.regex.GeneralizedTransitionGraph;
import model.regex.OperatorAlphabet;
import model.regex.operators.KleeneStar;

public class KleeneStarDeX extends DeExpressionifier {

	private KleeneStar myKleeneStar;

	public KleeneStarDeX(OperatorAlphabet alph) {
		super(alph);
		myKleeneStar = alph.getKleeneStar();
	}

	@Override
	public List<FSTransition> adjustTransitionSet(
			FSTransition trans, GeneralizedTransitionGraph gtg) {
		SymbolString input = trans.getInput();
		trans.setInput(input.subList(0,input.size()-1));
		List<FSTransition> toAdd = new ArrayList<FSTransition>();
		//do forward lambda transition
		toAdd.add(new FSTransition(trans.getFromState(), 
												trans.getToState()));
		//do backward lambdaTransition
		toAdd.add(new FSTransition(trans.getToState(), 
											trans.getFromState()));
		
		return toAdd;
	}

	@Override
	protected boolean isApplicable(SymbolString first, SymbolString rest) {
		return rest.isEmpty() && first.endsWith(myKleeneStar);
	}



}
