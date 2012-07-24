package view.sets.edit;

import javax.swing.JComponent;

import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import view.action.sets.FinishConstructionAction;
import view.sets.state.CreateState;
import view.sets.state.ModifyState;

import model.sets.AbstractSet;
import model.undo.UndoKeeper;

public class EditingPanelFactory {
	
	public static SetsEditingPanel createNewEditingPanel (UndoKeeper keeper) {
		SetsEditingPanel editor = new SetsEditingPanel(keeper);
		return editor;
	}
	
	
	public static JComponent createPanelFromSet (UndoKeeper keeper, AbstractSet set) {
		SetsEditingPanel editor = new SetsEditingPanel(keeper);
		SetDefinitionPanel definition = new SetDefinitionPanel(keeper);
		definition.createFromExistingSet(set);
		FinishConstructionAction fin = new FinishConstructionAction(keeper, new ModifyState(definition, set));
		editor.expandView(definition);
		editor.expandView(new MagnifiableButton(fin, JFLAPPreferences.getDefaultTextSize()));
		return editor;
	}
	


}
