package view.automata.undoing;

import model.automata.State;
import view.automata.AutomatonEditorPanel;
import view.automata.Note;

public class StateLabelRenameEvent extends NoteRenameEvent {
	private AutomatonEditorPanel myPanel;
	private State myState;

	public StateLabelRenameEvent(AutomatonEditorPanel panel, State s, String oldText) {
		super(panel.getStateLabel(s), oldText);
		myPanel = panel;
		myState = s;
	}
	
	@Override
	public boolean redo() {
		boolean sup = super.redo();
		myPanel.moveStateLabel(myState);
		return sup;
	}
	
	@Override
	public boolean undo() {
		boolean sup = super.undo();
		myPanel.moveStateLabel(myState);
		return sup;
	}

}
