package view.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.undo.IUndoRedo;
import model.undo.UndoKeeper;

public abstract class UndoingAction extends AbstractAction {

	private UndoKeeper myKeeper;
	private IUndoRedo myEvent;

	public UndoingAction(String name, UndoKeeper keeper) {
		super(name);
		myKeeper = keeper;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		myEvent = createEvent(e);
		if (myEvent == null) return;
		myKeeper.beginCombine(myEvent);
		myEvent.redo();
		myKeeper.endCombine();
		
	}

	public abstract IUndoRedo createEvent(ActionEvent e);

}
