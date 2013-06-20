package view.automata.tools;

import java.awt.event.MouseEvent;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import view.automata.AutomatonEditorPanel;

public class StateTool<T extends Automaton<S>, S extends Transition<S>> extends
		EditingTool<T, S> {

	private State myState;

	public StateTool(AutomatonEditorPanel<T, S> panel) {
		super(panel);
		this.myState = null;
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
		AutomatonEditorPanel<T, S> panel = getPanel();
		if (e.getButton() == MouseEvent.BUTTON1)
			myState = panel.createState(e.getPoint());
		else if (e.getButton() == MouseEvent.BUTTON3) {
			Object atPoint = panel.objectAtPoint(e.getPoint());
			if (atPoint instanceof State)
				myState = (State) atPoint;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (myState != null)
			getPanel().moveState(myState, e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		myState = null;
	}
}
