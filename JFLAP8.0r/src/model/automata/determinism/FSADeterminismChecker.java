package model.automata.determinism;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import model.automata.Automaton;
import model.automata.SingleInputTransition;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class FSADeterminismChecker extends DeterminismChecker<FSATransition> {

	@Override
	protected SymbolString retrieveApplicableString(FSATransition trans) {
		return new SymbolString(trans.getInput());
	}
	
	@Override
	public boolean isDeterministic(Automaton<FSATransition> m) {
		return super.isDeterministic(m) &&
				getLambdaTransitions(m).isEmpty();
	}
	
	public Collection<FSATransition> getLambdaTransitions(Automaton<FSATransition> m){
		Collection<FSATransition> lambdas = new ArrayList<FSATransition>();
		for (FSATransition t: m.getTransitions()){
			if (t.isLambdaTransition()){
				lambdas.add(t);
			}
		}
		return lambdas;
	}


}
