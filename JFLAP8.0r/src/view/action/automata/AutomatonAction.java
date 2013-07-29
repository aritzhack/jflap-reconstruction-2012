package view.action.automata;

import javax.swing.AbstractAction;

import model.automata.Automaton;
import model.automata.Transition;
import view.automata.AutomatonEditorPanel;
import view.automata.views.AutomataView;

public abstract class AutomatonAction extends AbstractAction{

	public static final int PADDING = 150;
	private AutomataView myView;

	public AutomatonAction(String name, AutomataView view){
		super(name);
		myView = view;
	}
	
	public AutomataView getView() {
		return myView;
	}
	
	public AutomatonEditorPanel getEditorPanel() {
		return (AutomatonEditorPanel) myView.getCentralPanel();
	}
	
	public Automaton getAutomaton() {
		return myView.getDefinition();
	}
}
