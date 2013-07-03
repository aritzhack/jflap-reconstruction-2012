package view.automata.undoing;

import java.awt.Panel;
import java.awt.geom.Point2D;
import java.util.List;

import view.automata.AutomatonEditorPanel;

import model.automata.State;
import model.graph.ControlPoint;
import model.undo.IUndoRedo;

/**
 * Undo event for moving the ControlPoint of an edge in a TransitionGraph.
 * @author Ian McMahon
 *
 */
public class ControlMoveEvent implements IUndoRedo{

	private State myFrom;
	private State myTo;
	private Point2D pFrom;
	private Point2D pTo;
	private AutomatonEditorPanel myPanel;

	public ControlMoveEvent(AutomatonEditorPanel panel, State[] states, Point2D origin, Point2D dest){
		myPanel = panel;
		myFrom = states[0];
		myTo = states[1];
		pFrom = origin;
		pTo = dest;
	}
	
	@Override
	public boolean undo() {
		myPanel.clearSelection();
		setLocation(pFrom);
		return true;
	}

	@Override
	public boolean redo() {
		myPanel.clearSelection();
		setLocation(pTo);
		return true;
	}
	
	private void setLocation(Point2D point){
		myPanel.moveCtrlPoint(myFrom, myTo, point);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Control Point Moved";
	}
	
}
