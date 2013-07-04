package view.automata.views;

import javax.swing.JComponent;

import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.undo.UndoKeeper;

public class PDAView extends AutomataView<PushdownAutomaton, PDATransition> {

	public PDAView(PushdownAutomaton model) {
		super(model);
	}

}
