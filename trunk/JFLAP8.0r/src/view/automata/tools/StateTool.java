package view.automata.tools;

import java.awt.event.MouseEvent;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import view.automata.AutomatonEditorPanel;

public class StateTool<T extends Automaton<S>, S extends Transition<S>> extends Tool {
	
	
	private AutomatonEditorPanel<T, S> myPanel;
	private State myState;

	public StateTool(AutomatonEditorPanel<T, S> p){
		myPanel = p;
		myState = null;
	}

	@Override
	public String getToolTip() {
		return "State Creator";
	}

	@Override
	public char getActivatingKey() {
		return 's';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/state.gif";
	}

	
	@Override
	public void mousePressed(MouseEvent e) {
		//UndoKeeper stuff?
		if(e.getButton() == MouseEvent.BUTTON1)
			myState = myPanel.createState(e.getPoint());
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
			myPanel.moveState(myState, e.getPoint());
	}
}
