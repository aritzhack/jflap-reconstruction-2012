package view.util.undo.old;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SingleSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicBorders;

import view.util.SuperMouseAdapter;

public abstract class AboveSelectPopupMenu extends JPopupMenu {

	
	private List<JMenuItem> mySelected;
	private static final Color DESELECTED = Color.WHITE,
			SELECTED = UIManager.getDefaults().getColor("List.selectionBackground");
;

	public AboveSelectPopupMenu(){
		mySelected = new ArrayList<JMenuItem>();
	}

	protected void selectAbove(JMenuItem item) {
		this.selectAbove(this.getComponentIndex(item));
	}
	
	protected void selectAbove(int curr) {
		this.clearSelected();
		while(curr >= 0){
			JMenuItem item = (JMenuItem)this.getComponent(curr);
			this.select(item);
			curr--;
		}		
		this.revalidate();
	}
	
	private void select(JMenuItem item) {
		item.setBackground(SELECTED);
		mySelected.add(item);
	}
	
	private void deselect(JMenuItem item) {
		item.setBackground(DESELECTED);
	}

	protected void clearSelected() {
		for (Component c: this.getComponents()){
			this.deselect((JMenuItem) c);
		}
		mySelected.clear();
	}
	
	protected List<JMenuItem> getSelectedItems(){
		return mySelected;
	}
	
	

	@Override
	public JMenuItem add(JMenuItem item) {
		super.add(item);
		this.setupListeners(item);
		item.setBackground(DESELECTED);
		item.setBorder(BorderFactory.createEtchedBorder());
		return item;
	}

	public void setupListeners(JMenuItem item) {
		for (MouseListener listener: item.getMouseListeners()){
			item.removeMouseListener(listener);
		}
		for (MouseMotionListener listener: item.getMouseMotionListeners()){
			item.removeMouseMotionListener(listener);
		}
		item.addMouseListener(new SuperMouseAdapter() {
			
			@Override
			public void mouseExited(MouseEvent e) {
				AboveSelectPopupMenu.this.clearSelected();
			}

			
			@Override
			public void mouseReleased(MouseEvent event) {
				AboveSelectPopupMenu.this.doClick();
				AboveSelectPopupMenu.this.setVisible(false);
			}
	
			@Override
			public void mouseEntered(MouseEvent event) {
				Object item = event.getSource();
				if (item instanceof JMenuItem)
					AboveSelectPopupMenu.this.selectAbove((JMenuItem) item);
			}
			
		});
	}

	protected abstract void doClick();
	
}
