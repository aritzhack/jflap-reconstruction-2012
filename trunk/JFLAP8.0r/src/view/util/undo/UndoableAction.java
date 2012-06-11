package view.util.undo;


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import errors.BooleanWrapper;
import errors.JFLAPError;



public abstract class UndoableAction extends AbstractAction {

	
	
	public UndoableAction(String name) {
		super(name);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.redo();
	}

	public abstract boolean undo();

	public abstract boolean redo();

	
}
