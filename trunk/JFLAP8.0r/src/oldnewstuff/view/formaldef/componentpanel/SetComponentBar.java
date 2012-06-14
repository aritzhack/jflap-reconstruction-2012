package oldnewstuff.view.formaldef.componentpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.text.JTextComponent;

import oldnewstuff.view.JFLAPGUIResources;
import oldnewstuff.view.util.thinscroller.ThinScrollBarScrollPane;
import preferences.JFLAPPreferences;



import debug.JFLAPDebug;

import model.JFLAPConstants;
import model.formaldef.components.symbols.Symbol;


public abstract class SetComponentBar<T> extends JPanel implements JFLAPGUIResources{

	private JToolBar myBar;
	private Color myHighlight;
    private JTextComponent myFocus;
    
	public SetComponentBar(Color highlight) {
		JScrollPane view = new ThinScrollBarScrollPane(4, 
				JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		myBar = createBar();
		
		myHighlight = highlight;
		this.add(createLabel("{"));
		this.add(view);
		this.add(createLabel("}"));
		setTo();
		view.setViewportView(myBar);
	}
	
	private JToolBar createBar() {
		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		bar.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				doBarClickResponse(e);
			};
		} );

		return bar;
	}

	public void setTo(T ...items){
		myBar.removeAll();
		this.add(items);
		myBar.add(Box.createHorizontalGlue());
	}
	
	
	private JLabel createLabel(String string) {
		JLabel label = new JLabel(string);
		
		label.setFont(JFLAPPreferences.getFormalDefinitionFont());
		return label;
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
		box.setAlignmentX(LEFT_ALIGNMENT);
		box.setAlignmentY(CENTER_ALIGNMENT);
		return box;
	}

	public void removeAllBoxes(){
		myBar.removeAll();
	}
	
	public abstract void doClickResponse(T item, MouseEvent e);

	public abstract void doBarClickResponse(MouseEvent e);
	
	private class ItemBox extends JButton{

		private T myItem;

		public ItemBox(T item) {
			myItem = item;
			this.setOpaque(true);
			this.setText(myItem.toString());
			this.setFont(JFLAPPreferences.getFormalDefinitionFont());
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
