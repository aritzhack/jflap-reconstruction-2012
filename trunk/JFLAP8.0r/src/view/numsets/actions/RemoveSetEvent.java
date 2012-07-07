package view.numsets.actions;

import model.numbersets.AbstractNumberSet;
import model.numbersets.control.SetsManager;
import model.undo.IUndoRedo;

public class RemoveSetEvent implements IUndoRedo {
	
	private AbstractNumberSet mySet;
	
	public RemoveSetEvent (AbstractNumberSet set) {
		mySet = set;
	}

	@Override
	public boolean undo() {
		// TODO Auto-generated method stub
		SetsManager.ACTIVE_REGISTRY.add(mySet);
		return false;
	}

	@Override
	public boolean redo() {
		// TODO Auto-generated method stub
		SetsManager.ACTIVE_REGISTRY.remove(mySet);
		return false;
	}

	@Override
	public String getName() {
		return "Delete set";
	}

}
