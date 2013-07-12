package view.action.automata;

import java.awt.Dimension;

import javax.swing.AbstractAction;

import universe.JFLAPUniverse;
import view.automata.AutomatonEditorPanel;
import view.automata.views.AutomataView;

public abstract class AutomatonAction extends AbstractAction{

	public static final int PADDING = 150;
	private AutomataView myView;

	public AutomatonAction(String name, AutomataView view){
		super(name);
		myView = view;
	}
	
	public AutomatonEditorPanel getEditorPanel() {
		return (AutomatonEditorPanel) myView.getCentralPanel();
	}
}
