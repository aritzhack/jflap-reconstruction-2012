package view.sets.state;

import model.sets.AbstractSet;
import model.sets.CustomFiniteSet;
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
	private AbstractSet mySet;
	
	public ModifyState(SetDefinitionPanel source, AbstractSet set) {
		mySource = source;
		mySet = set;
	}

	@Override
	public AbstractSet finish(UndoKeeper keeper) {
		// TODO 
		
		return mySet;
		
	}


	@Override
	public SetsEditingPanel createEditingPanel(UndoKeeper keeper) {
		// TODO Auto-generated method stub
		SetsEditingPanel editor = new SetsEditingPanel(keeper);
		editor.createFromExistingSet(mySet);
		return editor;
	}

}
