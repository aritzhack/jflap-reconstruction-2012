package view.automata.undoing;

import view.automata.AutomatonEditorPanel;
import view.automata.Note;
import model.undo.IUndoRedo;

public class NoteRenameEvent extends SingleNoteEvent{

	private String myOldText;

	public NoteRenameEvent(AutomatonEditorPanel panel, Note n, String oldText){
		super(panel, n);
		myOldText = oldText;
	}
	
	@Override
	public boolean undo() {
		getNote().setText(myOldText);
		return true;
		}

	@Override
	public boolean redo() {
		return super.redo();
	}

	@Override
	public String getName() {
		return "Rename Note";
	}

}
