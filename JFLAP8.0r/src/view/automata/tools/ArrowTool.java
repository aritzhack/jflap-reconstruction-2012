package view.automata.tools;

import java.awt.event.MouseEvent;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.graph.ControlPoint;
import view.automata.AutomatonEditorPanel;

public class ArrowTool<T extends Automaton<S>, S extends Transition<S>> extends
		EditingTool<T, S> {

	private Object myObject;

	public ArrowTool(AutomatonEditorPanel<T, S> panel) {
		super(panel);
		myObject = null;
	}

	@Override
	public String getToolTip() {
		return "Attribute Editor";
	}

	@Override
	public char getActivatingKey() {
		return 'a';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/arrow.gif";
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			AutomatonEditorPanel<T, S> panel = getPanel();
			panel.clearSelection();
			myObject = panel.objectAtPoint(e.getPoint());

			if (myObject != null) {
				if ((e.getClickCount() == 1 && myObject instanceof State))
					panel.selectObject(myObject);
				if ((e.getClickCount() == 2 && myObject instanceof Transition))
					panel.editTransition((S) myObject);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (myObject instanceof State)
			getPanel().clearSelection();
		myObject = null;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (myObject != null) {
			AutomatonEditorPanel<T, S> panel = getPanel();

			if (myObject instanceof State)
				panel.moveState((State) myObject, e.getPoint());
			if (myObject instanceof State[]) {
				State from = ((State[]) myObject)[0], to = ((State[]) myObject)[1];
				panel.moveCtrlPoint(from, to, e.getPoint());
			}
		}
	}
}
