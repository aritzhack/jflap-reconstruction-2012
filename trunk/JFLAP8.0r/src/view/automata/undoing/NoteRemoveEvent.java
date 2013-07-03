package view.automata.undoing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.undo.IUndoRedo;
import view.automata.AutomatonEditorPanel;
import view.automata.Note;

public class NoteRemoveEvent implements IUndoRedo {

	private AutomatonEditorPanel myPanel;
	private Collection<Note> myNotes;
	private List<String> myStrings;

	public NoteRemoveEvent(AutomatonEditorPanel panel, Collection<Note> notes) {
		myPanel = panel;
		myNotes = notes;
		myStrings = new ArrayList<String>();
		
		for(Note n : myNotes)
			myStrings.add(n.getText());
	}

	@Override
	public boolean undo() {
		Note[] notes = myNotes.toArray(new Note[0]);
		for (int i = 0; i < notes.length; i++) {
			Note n = notes[i];
			myPanel.addNote(n);
			n.setText(myStrings.get(i));
		}
		myPanel.stopAllEditing();
		myPanel.repaint();
		return true;
	}

	@Override
	public boolean redo() {
		for (Note n : myNotes)
			myPanel.removeNote(n);
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Remove multiple notes";
	}

}
