package view.sets.state;

import model.sets.AbstractSet;
import model.sets.CustomFiniteSet;
import model.sets.SetsManager;
import model.undo.UndoKeeper;
import view.sets.edit.SetDefinitionPanel;
import view.sets.edit.SetsEditingPanel;

/**
 * State for modifying an existing set
 *
 *
 */
public class ModifyState extends State {
	
	private SetDefinitionPanel mySource;
	private AbstractSet myOriginalSet;
	private AbstractSet myModifiedSet;
	
	public ModifyState(SetDefinitionPanel source, AbstractSet set) {
		mySource = source;
		myOriginalSet = set;
	}

	@Override
	public AbstractSet finish(UndoKeeper keeper) {
		// TODO 
		
		return myOriginalSet;
		
	}


	@Override
	public SetsEditingPanel createEditingPanel(UndoKeeper keeper) {
		SetsEditingPanel editor = new SetsEditingPanel(keeper);
		editor.createFromExistingSet(myOriginalSet);
		return editor;
	}

	@Override
	public boolean undo() {
		SetsManager.ACTIVE_REGISTRY.remove(myModifiedSet);
		SetsManager.ACTIVE_REGISTRY.add(myOriginalSet);
		return true;
	}

	@Override
	public boolean redo() {
		SetsManager.ACTIVE_REGISTRY.add(myModifiedSet);
		SetsManager.ACTIVE_REGISTRY.remove(myModifiedSet);
		return true;
	}

}
