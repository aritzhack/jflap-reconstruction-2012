package view.sets.state;

import model.sets.AbstractSet;
import model.undo.UndoKeeper;
import view.sets.edit.SetsEditingPanel;

public abstract class State {

	
	public abstract SetsEditingPanel createEditingPanel(UndoKeeper keeper);
	
	/**
	 * Called by FinishConstructionAction to get the abstract set created/modified
	 * @param keeper
	 * @return TODO
	 * @throws Exception 
	 */
	public abstract AbstractSet finish(UndoKeeper keeper) throws Exception;
}
