package view.automata.undoing;

import java.awt.Point;

import view.automata.AutomatonEditorPanel;
import view.automata.Note;

import model.undo.IUndoRedo;

public class NoteMoveEvent implements IUndoRedo{
	
	private AutomatonEditorPanel myPanel;
	private Note myNote;
	private Point from;
	private Point to;
	private String myString;

	public NoteMoveEvent(AutomatonEditorPanel panel, Note n, Point old){
		myPanel = panel;
		myNote = n;
		from = old;
		to = n.getPoint();
		
		from.x = (int) Math.max(from.x, 0);
		from.y = (int) Math.max(from.y, 0);
		to.x = (int) Math.max(to.x, 0);
		to.y = (int) Math.max(to.y, 0);
		
		myString = n.getText();
	}

	@Override
	public boolean undo() {
		return move(from);
	}

	@Override
	public boolean redo() {
		return move(to);
	}

	@Override
	public String getName() {
		return "Move note";
	}
	
	private boolean move(Point dest){
		myNote.setText(myString);
		myNote.setPoint(dest);
		
		myPanel.clearSelection();
		myPanel.stopAllEditing();
		return true;
	}

}
