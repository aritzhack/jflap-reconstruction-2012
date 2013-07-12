package view.automata;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JLabel;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import util.JFLAPConstants;
import util.view.magnify.MagnifiablePanel;

public class AutomatonDisplayPanel<T extends Automaton<S>, S extends Transition<S>> extends MagnifiablePanel{
	
	private static final int PADDING = 150;
	private AutomatonEditorPanel<T, S> myPanel;

	public AutomatonDisplayPanel(AutomatonEditorPanel<T, S> editor, String name){
		super(new BorderLayout());
		setName(name);
		T auto = editor.getAutomaton();
		
		myPanel = new AutomatonEditorPanel<T, S>(auto, new UndoKeeper(), false);
		TransitionSet<S> tranSet = auto.getTransitions();
		
		for(State s : auto.getStates()){
			myPanel.moveState(s, editor.getPointForVertex(s));
		}
		
		for(S t : tranSet){
			State[] states = new State[]{t.getFromState(), t.getToState()};
			myPanel.moveCtrlPoint(states[0], states[1], editor.getControlPoint(states));
		}
		
		add(myPanel, BorderLayout.CENTER);
	}
	
	public void update(){
		myPanel.updateBounds(JFLAPUniverse.getActiveEnvironment().getGraphics());
		
		Dimension panDim = myPanel.getPreferredSize();
		int width = Math.max(getViewSize().width, panDim.width)+JFLAPConstants.STATE_RADIUS+5;
		setPreferredSize(new Dimension(width, panDim.height + PADDING));
	}
	
	public AutomatonEditorPanel<T, S> getPanel(){
		return myPanel;
	}
	
	private Dimension getViewSize() {
		return JFLAPUniverse.getActiveEnvironment().getPreferredSize();
	}

}
