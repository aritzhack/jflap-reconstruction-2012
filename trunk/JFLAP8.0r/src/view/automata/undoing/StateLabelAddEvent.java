package view.automata.undoing;

import java.awt.Point;
import java.awt.Rectangle;

import model.automata.State;
import model.undo.IUndoRedo;
import view.automata.AutomatonEditorPanel;
import view.automata.Note;

public class StateLabelAddEvent extends EditingEvent{
	
	private Note myLabel;
	private String myText;
	private State myState;

	public StateLabelAddEvent(AutomatonEditorPanel panel, State s, String text){
		super(panel);
		myState = s;
		myText = text;
		
		Point center = (Point) panel.getPointForVertex(s);
		myLabel = new Note(panel, center);
		myLabel.setBounds(new Rectangle(center, myLabel.getPreferredSize()));
	}
	
	@Override
	public boolean undo() {
		getPanel().removeStateLabel(myState);
		return true;
	}

	@Override
	public boolean redo() {
		getPanel().addStateLabel(myState, myLabel, myText);
		return true;
	}

	@Override
	public String getName() {
		return "Adds a state label";
	}

}
