package view;

import java.util.Map;

import view.util.undo.UndoKeeper;

public abstract class PrimaryEditingPane extends EditingPanel {

	public PrimaryEditingPane(boolean editable) {
		super(new UndoKeeper(), editable);
	}
	
}
