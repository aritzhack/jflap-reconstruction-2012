package view.automata;

import java.awt.geom.Point2D;

import debug.JFLAPDebug;

import model.automata.State;
import model.undo.IUndoRedo;

public class StateMoveEvent implements IUndoRedo {
	private State myState;
	private Point2D myFrom, myTo;
	private AutomatonEditorPanel myPanel;
	
	public StateMoveEvent (AutomatonEditorPanel panel, State vertex, Point2D from, Point2D to){
		myPanel = panel;
		myState = vertex;
		myFrom = from;
		myTo = to;
	}
	
	@Override
	public boolean undo() {
		myPanel.moveState(myState, myFrom);
		return true;
	}

	@Override
	public boolean redo() {
		myPanel.moveState(myState, myTo);
		return true;
	}

	@Override
	public String getName() {
		return "Move State";
	}
}
