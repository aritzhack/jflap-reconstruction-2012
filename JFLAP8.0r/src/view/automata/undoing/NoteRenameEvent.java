package view.automata.undoing;

import view.automata.Note;
import model.undo.IUndoRedo;

public class NoteRenameEvent implements IUndoRedo{

	private String myOldText;
	private Note myNote;
	private String myNewText;

	public NoteRenameEvent(Note n, String oldText){
		myOldText = oldText;
		myNote = n;
		myNewText = n.getText();
	}
	
	@Override
	public boolean undo() {
		myNote.setText(myOldText);
		return true;
		}

	@Override
	public boolean redo() {
		myNote.setText(myNewText);
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Rename Note";
	}

}
