package view.automata;

import java.awt.geom.Point2D;

import model.automata.Automaton;
import model.automata.State;
import model.undo.IUndoRedo;
import util.JFLAPConstants;
import util.view.GraphHelper;

/**
 * Undoing Event for moving a State from one specific point to another, such that
 * when undone and redone, the state returns to an onscreen point.
 * 
 * @author Ian McMahon
 * 
 */
public class StateMoveEvent implements IUndoRedo {
	private State myState;
	private Point2D myFrom, myTo;
	private AutomatonEditorPanel myPanel;
	private Automaton myDefinition;

	public StateMoveEvent(AutomatonEditorPanel panel, Automaton def,
			State vertex, Point2D from, Point2D to) {
		myPanel = panel;
		myDefinition = def;
		myState = vertex;
		myFrom = from;
		myTo = to;
	}

	@Override
	public boolean undo() {
		myFrom = GraphHelper.getOnscreenPoint(
				myState.equals(myDefinition.getStartState()), myFrom);
		myPanel.moveState(myState, myFrom);
		return true;
	}

	@Override
	public boolean redo() {
		myTo = GraphHelper.getOnscreenPoint(
				myState.equals(myDefinition.getStartState()), myTo);
		myPanel.moveState(myState, myTo);
		return true;
	}

	@Override
	public String getName() {
		return "Move State";
	}
}
