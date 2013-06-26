package view.automata.tools;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import util.Point2DAdv;
import view.automata.AutomatonEditorPanel;
import view.automata.ControlMoveEvent;
import view.automata.StateMoveEvent;

/**
 * Tool for selection and editing of Automaton graphs.
 * 
 * @author Ian McMahon
 */
public class ArrowTool<T extends Automaton<S>, S extends Transition<S>> extends
		EditingTool<T, S> {

	private Object myObject;
	private Point2D myInitialPoint;

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
		AutomatonEditorPanel<T, S> panel = getPanel();

		if (SwingUtilities.isLeftMouseButton(e)) {
			// reinitialize all fields
			panel.clearSelection();
			myObject = panel.objectAtPoint(e.getPoint());
			myInitialPoint = e.getPoint();

			if (isStateClicked(e) || isTransitionClicked(e)) {
				// States and Transitions must be selected
				panel.selectObject(myObject);

				if (myObject instanceof State) {
					myInitialPoint = new Point2DAdv(
							panel.getPointForVertex((State) myObject));
				} else
					panel.editTransition((S) myObject, false);
			} else if (myObject instanceof State[])
				myInitialPoint = panel.getControlPoint((State[]) myObject);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		AutomatonEditorPanel<T, S> panel = getPanel();

		if (SwingUtilities.isLeftMouseButton(e)) {
			if (myObject instanceof State)
				// Move the state
				panel.moveState((State) myObject, e.getPoint());
			else if (myObject instanceof State[]) {
				// Move the control point
				State from = ((State[]) myObject)[0], to = ((State[]) myObject)[1];
				panel.moveCtrlPoint(from, to, e.getPoint());
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		AutomatonEditorPanel<T, S> panel = getPanel();

		if (myObject instanceof State) {
			// Clear the selection and notify the undo keeper
			panel.clearSelection();
			getKeeper().registerChange(
					new StateMoveEvent(panel, (State) myObject, myInitialPoint,
							e.getPoint()));
		} else if (myObject instanceof State[]) {
			// Notify the undo keeper
			panel.getKeeper().registerChange(
					new ControlMoveEvent(panel, (State[]) myObject,
							myInitialPoint, e.getPoint()));
		}
		myInitialPoint = null;
	}

	/** Returns true if the selected object is a State and the user clicked once. */
	private boolean isStateClicked(MouseEvent e) {
		return e.getClickCount() == 1 && myObject instanceof State;
	}
	
	/** Returns true if the selected object is a Transition and the user double clicked. */
	private boolean isTransitionClicked(MouseEvent e) {
		return e.getClickCount() == 2 && myObject instanceof Transition;
	}
}
