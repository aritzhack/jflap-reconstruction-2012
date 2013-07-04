package view.automata.views;

import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;

public class FSAView extends AutomataView<FiniteStateAcceptor, FSATransition>{

	public FSAView(FiniteStateAcceptor acceptor){
		super(acceptor);
	}
}
