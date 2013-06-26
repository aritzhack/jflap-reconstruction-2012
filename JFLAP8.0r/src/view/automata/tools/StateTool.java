package view.automata.tools;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import model.automata.Automaton;
import model.automata.State;
import model.automata.StateSet;
import model.automata.Transition;
import util.Point2DAdv;
import view.automata.AutomatonEditorPanel;
import view.automata.StateAddEvent;
import view.automata.StateMoveEvent;

public class StateTool<T extends Automaton<S>, S extends Transition<S>> extends
		EditingTool<T, S> {

	private State myState;
	private StateSet myStateSet;
	private Point2D myPoint;

	public StateTool(AutomatonEditorPanel<T, S> panel, StateSet s) {
		super(panel);
		myStateSet = s;
		this.myState = null;
		myPoint = null;
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
		//Left click, create a state, move it to mouse's location
		if (SwingUtilities.isLeftMouseButton(e)){
			myState = panel.createState(e.getPoint());
		}
		//Right click, allow for dragging other states
		else if (SwingUtilities.isRightMouseButton(e)) {
			Object o = panel.objectAtPoint(e.getPoint());
			if (o instanceof State){
				myState = (State) o;
			}
		}
		if(myState != null){
			getPanel().selectObject(myState);
			myPoint = new Point2DAdv(panel.getPointForVertex(myState));
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (myState != null)
			getPanel().moveState(myState, e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(myState != null){
			AutomatonEditorPanel panel = getPanel();
			panel.clearSelection();
			
			if(SwingUtilities.isLeftMouseButton(e))
				panel.getKeeper().registerChange(new StateAddEvent(panel, myStateSet, myState, e.getPoint()));
			else if(!myPoint.equals(e.getPoint()))
				panel.getKeeper().registerChange(new StateMoveEvent(panel, myState, myPoint, e.getPoint()));
		}
		myState = null;
		myPoint = null;
	}
}
