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
		if (!setFields(e)) return;
		boolean bw = this.redo();
	}

	protected abstract boolean setFields(ActionEvent e);
	

	public abstract boolean undo();

	public abstract boolean redo();

	
}
