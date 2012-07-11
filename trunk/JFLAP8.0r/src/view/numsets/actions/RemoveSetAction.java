package view.numsets.actions;

import java.awt.event.ActionEvent;
import java.util.AbstractSet;

import model.sets.AbstractNumberSet;
import model.sets.SetsManager;
import model.undo.IUndoRedo;
import model.undo.UndoKeeper;
import view.action.UndoingAction;

public class RemoveSetAction extends UndoingAction {
	
	public RemoveSetAction (UndoKeeper keeper) {
		super("Delete", keeper);
	}

	@Override
	public IUndoRedo createEvent(ActionEvent e) {
		// TODO Auto-generated method stub
		
		AbstractSet set = (AbstractSet) e.getSource();
		
		SetsManager.ACTIVE_REGISTRY.remove(set);
		
		return null;
	}


}
