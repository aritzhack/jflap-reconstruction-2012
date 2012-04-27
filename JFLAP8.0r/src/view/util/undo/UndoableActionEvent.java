package view.util.undo;

import view.util.undo.UndoKeeper.UndoableActionType;

public class UndoableActionEvent {

	private UndoKeeper myKeeper;
	private UndoableActionType myType;
	private UndoableAction myAction;

	public UndoableActionEvent(UndoKeeper undoKeeper, 
			UndoableActionType help,
			UndoableAction peek) {
		setKeeper(undoKeeper);
		setType(help);
		setAction(peek);
		
	}

	public UndoKeeper getKeeper() {
		return myKeeper;
	}

	public void setKeeper(UndoKeeper myKeeper) {
		this.myKeeper = myKeeper;
	}

	public UndoableActionType getType() {
		return myType;
	}

	public void setType(UndoableActionType myType) {
		this.myType = myType;
	}

	public UndoableAction getAction() {
		return myAction;
	}

	public void setAction(UndoableAction myAction) {
		this.myAction = myAction;
	}

	
	
	
}
