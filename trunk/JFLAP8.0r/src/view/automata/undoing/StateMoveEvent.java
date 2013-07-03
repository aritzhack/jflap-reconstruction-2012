package view.automata.undoing;

import java.awt.geom.Point2D;

import model.automata.Automaton;
import model.automata.State;
import model.undo.IUndoRedo;
import util.JFLAPConstants;
import util.view.GraphHelper;
import view.automata.AutomatonEditorPanel;

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
				Automaton.isStartState(myDefinition, myState), myFrom);
		return moveAndClear(myFrom);
	}

	@Override
	public boolean redo() {
		myTo = GraphHelper.getOnscreenPoint(
				Automaton.isStartState(myDefinition, myState), myTo);
		return moveAndClear(myTo);
	}

	@Override
	public String getName() {
		return "Move State";
	}
	
	private boolean moveAndClear(Point2D to){
		myPanel.moveState(myState, to);
		myPanel.clearSelection();
		myPanel.repaint();
		return true;
	}
}
