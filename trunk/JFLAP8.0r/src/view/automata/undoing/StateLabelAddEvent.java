package view.automata.undoing;

import java.awt.Point;
import java.awt.Rectangle;

import model.automata.State;
import model.undo.IUndoRedo;
import view.automata.AutomatonEditorPanel;
import view.automata.Note;

public class StateLabelAddEvent implements IUndoRedo{

	private AutomatonEditorPanel myPanel;
	private Note myLabel;
	private String myText;
	private State myState;

	public StateLabelAddEvent(AutomatonEditorPanel panel, State s, String text){
		myPanel = panel;
		myState = s;
		myText = text;
		
		Point center = (Point) panel.getPointForVertex(s);
		myLabel = new Note(myPanel, center);
		myLabel.setBounds(new Rectangle(center, myLabel.getPreferredSize()));
	}
	
	@Override
	public boolean undo() {
		myPanel.removeStateLabel(myState);
		return true;
	}

	@Override
	public boolean redo() {
		myPanel.addStateLabel(myState, myLabel, myText);
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Adds a state label";
	}

}
