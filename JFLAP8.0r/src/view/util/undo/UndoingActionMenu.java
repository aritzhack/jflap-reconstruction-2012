package view.util.undo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class UndoingActionMenu extends JPopupMenu implements ActionListener{

	private UndoKeeper myKeeper;

	public UndoingActionMenu(UndoKeeper keeper, UndoableAction ... actions){
		myKeeper = keeper;
		for (UndoableAction a : actions){

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem item = (JMenuItem) e.getSource();
		Action a = item.getAction();
		if (a instanceof UndoableAction){
			myKeeper.registerAction((UndoableAction) a);
		}
	}

	@Override
	public JMenuItem add(Action a) {
		JMenuItem item = super.add(a);
		item.addActionListener(this);
		return item;
	}
	
	
	
}
