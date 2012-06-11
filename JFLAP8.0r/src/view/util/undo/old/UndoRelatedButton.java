package view.util.undo.old;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

import errors.BooleanWrapper;


public abstract class UndoRelatedButton extends JButton {

	private static final long DELAY = 5;
	private static final int NUM_ITEMS = 7;
	private Action myAction;
	
	public UndoRelatedButton(UndoKeeperAction a){
		super(a);
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3){
					UndoRelatedButton.this.showHistoryMenu(e);
				}
			}
		});
		myAction = a;
	}
	


	protected void showHistoryMenu(MouseEvent e) {
		AboveSelectPopupMenu menu = new AboveSelectPopupMenu(){

			@Override
			protected void doClick() {
				UndoRelatedButton.this.doMultiAction(this.getSelectedItems().size());
			}
			
		};
		Iterator<? extends Action> iter = this.getActionList().iterator();
		int i = 0;
		while (iter.hasNext() && i++ < NUM_ITEMS){
			menu.add((String)iter.next().getValue(Action.NAME));
		}
		menu.show((Component) e.getSource(), e.getX(), e.getY());
	}

	
	protected abstract Iterable<? extends Action> getActionList();
	
	protected abstract boolean doMultiAction(int n);
	
}
