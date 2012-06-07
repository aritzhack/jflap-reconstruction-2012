package model.automata.determinism;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import model.automata.Automaton;
import model.automata.Transition;
import model.automata.acceptors.fsa.FSTransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class FSADeterminismChecker extends DeterminismChecker<FSTransition> {

	@Override
	protected SymbolString retrieveApplicableString(FSTransition trans) {
		return trans.getInput();
	}
	
	@Override
	public boolean isDeterministic(Automaton<FSTransition> m) {
		return super.isDeterministic(m) &&
				getLambdaTransitions(m).isEmpty();
	}
	
	public Collection<FSTransition> getLambdaTransitions(Automaton<FSTransition> m){
		Collection<FSTransition> lambdas = new ArrayList<FSTransition>();
		for (FSTransition t: m.getTransitions()){
			if (t.isLambdaTransition()){
				lambdas.add(t);
			}
		}
		return lambdas;
	}


}
