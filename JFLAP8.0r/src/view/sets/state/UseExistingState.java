package view.sets.state;

import model.sets.AbstractSet;
import model.sets.SetsManager;
import model.undo.UndoKeeper;
import view.sets.SetsDropdownMenu;
import view.sets.edit.SetsEditingPanel;

public class UseExistingState extends State {
	
	private SetsDropdownMenu mySource;
	private AbstractSet mySet;
	
	public UseExistingState (SetsDropdownMenu source) {
		mySource = source;
	}

	@Override
	public SetsEditingPanel createEditingPanel(UndoKeeper keeper) {
		SetsEditingPanel editor = new SetsEditingPanel(keeper, false);
		editor.createFromExistingSet(mySet);
		return editor;
	}

	@Override
	public AbstractSet finish(UndoKeeper keeper) throws Exception {
		mySet = mySource.getSelectedSet();
		return mySet;
	}

	@Override
	public boolean undo() {
		SetsManager.ACTIVE_REGISTRY.remove(mySet);
		return true;
	}

	@Override
	public boolean redo() {
		SetsManager.ACTIVE_REGISTRY.add(mySet);
		return true;
	}

}
