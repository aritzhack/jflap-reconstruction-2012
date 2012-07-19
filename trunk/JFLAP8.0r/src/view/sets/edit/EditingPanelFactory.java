package view.sets.edit;

import javax.swing.JComponent;
import javax.swing.JPanel;

import model.sets.AbstractSet;
import model.undo.UndoKeeper;

public class EditingPanelFactory {
	
	public static JComponent createNewEditingPanel (UndoKeeper keeper) {
		SetsEditingPanel editor = new SetsEditingPanel(keeper);
		return editor;
	}
	
	
	public static JComponent createPanelFromSet (UndoKeeper keeper, AbstractSet set) {
		SetsEditingPanel editor = new SetsEditingPanel(keeper);
		editor.createFromExistingSet(set);
		return editor;
	}
	


}
