package view.formaldef.componentpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.text.JTextComponent;

import debug.JFLAPDebug;

import oldnewstuff.view.JFLAPGUIResources;
import oldnewstuff.view.util.thinscroller.ThinScrollBarScrollPane;
import util.JFLAPConstants;

import model.formaldef.components.symbols.Symbol;


public abstract class SetComponentBar<T> extends ThinScrollBarScrollPane implements JFLAPGUIResources{

	private JToolBar myBar;
	private Color myHighlight;
    private JTextComponent myFocus;
    
	public SetComponentBar(Color highlight) {
		super(2, HORIZONTAL_SCROLLBAR_AS_NEEDED, VERTICAL_SCROLLBAR_NEVER);
		myBar = new JToolBar();
		myBar.setFloatable(false);
		myHighlight = highlight;
		this.setViewportView(myBar);
	}
	
	public void setTo(T ...items){
		JFLAPDebug.print(Arrays.asList(items));
		this.removeAll();
		this.add(items);
	}
	
	
	public void add(T ...toAdd){
		for (T item: toAdd){
			myBar.add(setUpBox(item));
		}
	}
	
	public boolean highlightBoxes(T ... items){
		for (T item : items){
			ItemBox box = this.getBoxForItem(item);
			if (box == null) return false;
			box.highlight();
		}
		return true;
	}

	private ItemBox getBoxForItem(T item) {
		for (Component comp : this.getComponents()){
			if (comp instanceof SetComponentBar.ItemBox &&
					((SetComponentBar.ItemBox) comp).getItem().equals(item)){
				return (ItemBox) comp;
			}
		}
		
		return null;
	}

	public void clearHighlights() {
		for (Component comp : this.getComponents()){
			if (comp instanceof SetComponentBar.ItemBox){
				((SetComponentBar.ItemBox) comp).dehighlight();
			}
		}
	}

	
	private ItemBox setUpBox(T item) {
		ItemBox box = new ItemBox(item);
		box.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				doClickResponse(((ItemBox) e.getSource()).getItem(), e);
			}
		});
		return box;
	}

	public void removeAllBoxes(){
		myBar.removeAll();
	}
	
	public abstract void doClickResponse(T item, MouseEvent e);

	private class ItemBox extends JButton{

		private T myItem;

		public ItemBox(T item) {
			myItem = item;
			this.setOpaque(true);
			this.setText(myItem.toString());
		}
		
		public void dehighlight() {
			this.setBackground(DEFAULT_SWING_BG);
		}

		public void highlight(){
			this.setBackground(myHighlight);
		}
		
		public T getItem(){
			return myItem;
		}
		
	}
	
}
