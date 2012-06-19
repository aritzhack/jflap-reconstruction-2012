package view.undo;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import model.undo.UndoKeeper;
import model.undo.UndoKeeperListener;

public class UndoPanel extends JPanel implements UndoKeeperListener {

	private UndoButton myUndoButton;
	private RedoButton myRedoButton;

	public UndoPanel(UndoKeeper keeper){
		myUndoButton = new UndoButton(keeper);
		myRedoButton = new RedoButton(keeper);
		keeper.addUndoListener(this);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(myUndoButton);
		this.add(myRedoButton);
	}

	@Override
	public void keeperStateChanged() {
		myUndoButton.updateEnabled();
		myRedoButton.updateEnabled();
	}
	
}
