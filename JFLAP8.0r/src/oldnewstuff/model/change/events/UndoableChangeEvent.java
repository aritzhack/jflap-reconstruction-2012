package model.change.events;

import model.change.ChangeEvent;
import model.undo.IUndoRedo;

public abstract class UndoableChangeEvent extends ChangeEvent implements IUndoRedo {

	public UndoableChangeEvent(int type, Object source) {
		super(type, source);
	}

}
