package view.automata.undoing;

import model.undo.IUndoRedo;
import view.automata.AutomatonEditorPanel;
import view.automata.Note;
import view.automata.tools.EditingTool;

public class NoteAddEvent implements IUndoRedo{

	private AutomatonEditorPanel myPanel;
	private String myString;
	private Note myNote;

	public NoteAddEvent(AutomatonEditorPanel panel, Note n){
		myPanel = panel;
		myNote = n;
		myString = n.getText();
	}
	
	@Override
	public boolean undo() {
		myPanel.removeNote(myNote);
		return true;
	}

	@Override
	public boolean redo() {
		myPanel.addNote(myNote);
		myNote.setText(myString);
		myNote.setSelectionStart(0);
		myPanel.editNote(myNote);
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Note creation event";
	}

}
