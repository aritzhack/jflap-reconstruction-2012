package view.util.undo;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public abstract class UndoKeeperAction extends AbstractAction implements UndoableActionListener {


	private UndoKeeper myKeeper;

	public UndoKeeperAction(String string, UndoKeeper keepr){
		super(string);
		myKeeper = keepr;
		myKeeper.addUndoableActionListener(this);
	}
	
	public UndoKeeper getKeeper(){
		return myKeeper;
	}
	
}
