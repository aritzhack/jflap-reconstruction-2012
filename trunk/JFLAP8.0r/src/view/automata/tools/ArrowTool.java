package view.automata.tools;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

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
		if (SwingUtilities.isLeftMouseButton(e)) {
			AutomatonEditorPanel<T, S> panel = getPanel();
			panel.clearSelection();
			myObject = panel.objectAtPoint(e.getPoint());
			
			if (myObject != null) {
				if (isStateClicked(e) || isTransitionClicked(e)) {
					panel.selectObject(myObject);

					if (myObject instanceof Transition)
						panel.editTransition((S) myObject, false);
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (myObject instanceof State)
			getPanel().clearSelection();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			AutomatonEditorPanel<T, S> panel = getPanel();

			if (myObject != null) {
				if (myObject instanceof State)
					panel.moveState((State) myObject, e.getPoint());
				if (myObject instanceof State[]) {
					State from = ((State[]) myObject)[0], to = ((State[]) myObject)[1];
					panel.moveCtrlPoint(from, to, e.getPoint());
				}
			}
			
		}
	}

	private boolean isStateClicked(MouseEvent e) {
		return e.getClickCount() == 1 && myObject instanceof State;
	}

	private boolean isTransitionClicked(MouseEvent e) {
		return e.getClickCount() == 2 && myObject instanceof Transition;
	}
}
