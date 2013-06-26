package view.automata.transitiontable;

import model.automata.Automaton;
import model.automata.Transition;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import view.automata.AutomatonEditorPanel;

public class TransitionTableFactory {

	public static TransitionTable createTable(Transition trans, Automaton automaton, AutomatonEditorPanel panel){
		if(automaton instanceof FiniteStateAcceptor)
			return new FSATransitionTable((FSATransition) trans, (FiniteStateAcceptor) automaton, panel);
//		NOT YET IMPLEMENTED FOR OTHERS
//		if(automaton instanceof MultiTapeTuringMachine)
//			return new MultiTapeTMTransitionTable((MultiTapeTMTransition) trans, (MultiTapeTuringMachine) automaton, panel);
		return null;
	}
}
