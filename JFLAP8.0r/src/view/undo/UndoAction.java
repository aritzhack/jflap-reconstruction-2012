package view.undo;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import util.JFLAPConstants;


import model.undo.UndoKeeper;
import model.undo.UndoKeeperListener;

public class UndoAction extends AbstractAction implements UndoKeeperListener {

	private UndoKeeper myKeeper;

	public UndoAction(UndoKeeper keeper) {
		super("Undo");
		myKeeper = keeper;
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				JFLAPConstants.MAIN_MENU_MASK));
		keeper.addUndoListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		myKeeper.undoLast();
	}

	@Override
	public void keeperStateChanged() {
		this.setEnabled(myKeeper.canUndo());
	}
	
	

}
