package oldnewstuff.view.formaldef.componentpanel.alphabets;

import gui.undo.UndoKeeper;
import gui.undo.UndoableAction;
import gui.undo.UndoingActionMenu;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.text.JTextComponent;

import oldnewstuff.action.alphabets.ModifySymbolAction;
import oldnewstuff.action.alphabets.PromptAndAddSymbols;
import oldnewstuff.action.alphabets.RemoveSymbolsAction;
import oldnewstuff.view.formaldef.FormalDefinitionView;
import oldnewstuff.view.formaldef.componentpanel.DefinitionComponentPanel;
import oldnewstuff.view.formaldef.componentpanel.SetComponentBar;




import debug.JFLAPDebug;


import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;

public class AlphabetBar extends DefinitionComponentPanel<Alphabet> {

	private SymbolBar mySymbolBar;
	private JTextComponent myFocus;

	public AlphabetBar(Alphabet comp, UndoKeeper keeper, boolean editable) {
		super(comp, keeper, editable);
		setUpFocusManager();
	}

	@Override
	public void update(Alphabet a) {
		this.getSymbolBar().setTo(a.toArray(new Symbol[0]));		
	}

	private SymbolBar getSymbolBar() {
		if (mySymbolBar == null){
			mySymbolBar = new SymbolBar();
			this.add(mySymbolBar, LEFT_ALIGNMENT);
		}
		return mySymbolBar;
	}

	@Override
	public JPopupMenu getMenu() {
		UndoableAction a = new PromptAndAddSymbols(getComponent());
		return new UndoingActionMenu(getKeeper(),this, a);
	}

	/**
	 * Retrieve the right-click menu linked to the 
	 * individual symbol boxes.
	 * 
	 * @param item
	 * @return
	 */
	public JPopupMenu getBoxMenu(Symbol item) {
		Alphabet a = this.getComponent();
		JPopupMenu menu = this.getMenu();
//		menu.add(new RemoveSymbolsAction(a, getDefinitionParent(), item));
//		menu.add(new ModifySymbolAction(a, item));
		
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

		@Override
		public void doBarClickResponse(MouseEvent event) {
			if (event.getButton() == MouseEvent.BUTTON3)
				getMenu().show(event.getComponent(), event.getX(), event.getY());
		}
		
		
	}

	@Override
	public void cancelAllEditing() {
		
	}

	@Override
	public String getName() {
		return getModel().getDescriptionName();
	}

}
