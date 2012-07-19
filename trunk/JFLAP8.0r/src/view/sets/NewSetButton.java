package view.sets;

import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import view.action.sets.ActivateSetAction;

@SuppressWarnings("serial")
public class NewSetButton extends MagnifiableButton {
	

	public NewSetButton(UndoKeeper keeper) {
		super(new ActivateSetAction(keeper), JFLAPPreferences.getDefaultTextSize());
	}

	
	
}
