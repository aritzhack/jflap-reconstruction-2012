package view.formaldef.componentpanel.alphabets;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

import action.alphabets.ModifySymbolAction;
import action.alphabets.PromptAndAddSymbols;
import action.alphabets.RemoveSymbolsAction;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import view.formaldef.componentpanel.DefinitionComponentPanel;
import view.formaldef.componentpanel.SetComponentBar;
import view.util.undo.UndoKeeper;
import view.util.undo.UndoableAction;
import view.util.undo.old.UndoingActionMenu;

public class AlphabetBar<T extends Alphabet> extends DefinitionComponentPanel<T> {

	private SymbolBar mySymbolBar;
	private JTextComponent myFocus;

	public AlphabetBar(T comp, boolean editable, UndoKeeper keeper) {
		super(comp, editable, keeper);
		setUpLabels();
		setUpFocusManager();
	}

	private void setUpLabels() {
		this.add(new JLabel("{"));
		this.add(getSymbolBar());
		this.add(new JLabel("}"));
	}
	
	
	@Override
	public void update() {
		this.getSymbolBar().setTo(this.getComponent().toArray(new Symbol[0]));		
	}

	private SymbolBar getSymbolBar() {
		if (mySymbolBar == null)
			mySymbolBar = new SymbolBar();
		return mySymbolBar;
	}

	@Override
	public JPopupMenu getMenu() {
		UndoableAction a = new PromptAndAddSymbols(getComponent());
		return new UndoingActionMenu(getKeeper(), a);
	}

	/**
	 * Retrieve the right-click menu linked to the 
	 * individual symbol boxes.
	 * 
	 * @param item
	 * @return
	 */
	public JPopupMenu getBoxMenu(Symbol item) {
		T a = this.getComponent();
		JPopupMenu menu = this.getMenu();
		menu.add(new RemoveSymbolsAction(a, item));
		menu.add(new ModifySymbolAction(a, item));
		
		return menu;
	}
	
	public void addToCurrentTextFocus(Symbol item) {
		myFocus.replaceSelection(item.toString());
	}
	
	public Color getHighlightColor(){
		return DEFAULT_SWING_BG;
	}
	
	private void setUpFocusManager() {
		KeyboardFocusManager focusManager =
		    KeyboardFocusManager.getCurrentKeyboardFocusManager();
		focusManager.addPropertyChangeListener(
		    new PropertyChangeListener() {
		    	

				public void propertyChange(PropertyChangeEvent e) {
		            String prop = e.getPropertyName();
		            if (("focusOwner".equals(prop))){
		                  if (e.getNewValue() instanceof JTextComponent)
		                	  myFocus = (JTextComponent) e.getNewValue();
		                  else
		                	  myFocus = null;
		            }
	            }
		    }
		);		
	}
	
	private class SymbolBar extends SetComponentBar<Symbol>{

		public SymbolBar() {
			super(getHighlightColor());
		}

		@Override
		public void doClickResponse(Symbol item, MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3 && isEditable())
				getBoxMenu(item).show(e.getComponent(), e.getX(), e.getY());
			else if(e.getButton() == MouseEvent.BUTTON1){
				addToCurrentTextFocus(item);
			}
		}
		
		
	}	
	
	
}
