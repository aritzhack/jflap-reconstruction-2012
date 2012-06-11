package view.util.undo.old;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import view.util.undo.UndoKeeper;

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
