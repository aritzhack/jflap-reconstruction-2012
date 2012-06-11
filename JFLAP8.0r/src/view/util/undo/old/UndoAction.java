package view.util.undo.old;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import universe.Universe;
import view.util.undo.UndoKeeper;
import errors.BooleanWrapper;
import errors.JFLAPError;






public class UndoAction extends UndoKeeperAction {

	public UndoAction(UndoKeeper keeper) {
		super("Undo...", keeper);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				Universe.curProfile.getMainMenuMask()));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.getKeeper().undoLast();
	}

	@Override
	public void actionPerformed(UndoableActionEvent e) {
		this.setEnabled(this.getKeeper().canUndo());
	}

}
