package view.util.undo;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import errors.BooleanWrapper;
import errors.JFLAPError;
import universe.Universe;
import view.util.undo.UndoKeeper.UndoableActionType;




public class RedoAction extends UndoKeeperAction {

	public RedoAction(UndoKeeper keeper) {
		super("Redo...", keeper);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				Universe.curProfile.getMainMenuMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.getKeeper().redoLast();	
	}

	@Override
	public void actionPerformed(UndoableActionEvent e) {
		this.setEnabled(this.getKeeper().canRedo());
	}


}
