package view.automata;

import java.awt.geom.Point2D;

import model.automata.State;
import model.automata.StateSet;
import model.change.events.AddEvent;

public class StateAddEvent extends AddEvent<State> {

	private State myState;
	private Point2D myPoint;
	private AutomatonEditorPanel myPanel;

	public StateAddEvent(AutomatonEditorPanel panel, StateSet states, State vertex, Point2D p) {
		super(states, vertex);
		myPanel = panel;
		myState = vertex;
		myPoint = p;
	}

	@Override
	public boolean redo() {
		boolean sup = super.redo();
		myPanel.moveState(myState, myPoint);
		return sup;
	}
}
