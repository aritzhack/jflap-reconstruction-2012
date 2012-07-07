package view.numsets.actions;

import java.awt.event.ActionEvent;

import model.numbersets.AbstractNumberSet;
import model.numbersets.control.SetsManager;
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
		
		AbstractNumberSet set = (AbstractNumberSet) e.getSource();
		
		SetsManager.ACTIVE_REGISTRY.remove(set);
		
		return null;
	}


}
