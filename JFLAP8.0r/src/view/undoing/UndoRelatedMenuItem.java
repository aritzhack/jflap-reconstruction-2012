package view.undoing;

import javax.swing.JMenuItem;

import model.undo.UndoKeeper;
import model.undo.UndoKeeperListener;

public class UndoRelatedMenuItem extends JMenuItem implements UndoKeeperListener {

	public UndoRelatedMenuItem(UndoRelatedAction a) {
		super(a);
		a.getKeeper().addUndoListener(this);
		keeperStateChanged();
	}

	@Override
	public void keeperStateChanged() {
		this.setEnabled(this.getAction().isEnabled());
	}
	
}
