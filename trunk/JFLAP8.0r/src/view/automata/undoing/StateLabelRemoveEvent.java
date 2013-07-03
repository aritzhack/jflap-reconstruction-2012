package view.automata.undoing;

import model.automata.State;
import model.undo.IUndoRedo;
import view.automata.AutomatonEditorPanel;
import view.automata.Note;

public class StateLabelRemoveEvent implements IUndoRedo{

	private AutomatonEditorPanel myPanel;
	private State myState;
	private Note myNote;

	public StateLabelRemoveEvent (AutomatonEditorPanel panel, State s) {
		myPanel = panel;
		myState = s;
		myNote = panel.getStateLabel(s);
	}
	
	@Override
	public boolean undo() {
		myPanel.addStateLabel(myState, myNote, myNote.getText());
		return true;
	}

	@Override
	public boolean redo() {
		myPanel.removeStateLabel(myState);
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Remove State Label";
	}

}
