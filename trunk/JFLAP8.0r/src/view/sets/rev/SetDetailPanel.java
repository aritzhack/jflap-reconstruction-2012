package view.sets.rev;

import javax.swing.JComponent;

import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import util.view.magnify.MagnifiableSplitPane;
import util.view.magnify.MagnifiableTextField;
import view.EditingPanel;
import view.action.sets.ActivateSetAction;

public abstract class SetDetailPanel extends EditingPanel {

	private UndoKeeper myKeeper;
	
	public SetDetailPanel(UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		myKeeper = keeper;
		
		add(new StartEditingButton());
//		add(createMainPanel());
	}
	
	
	public JComponent createMainPanel() {
		MagnifiableSplitPane split = new MagnifiableSplitPane();
		
		return split;
	}
	
	
	@Override
	public String getName() {
		return "Set Editor";
	}

	
	@SuppressWarnings("serial")
	private class StartEditingButton extends MagnifiableButton {
		
		public StartEditingButton() {
			super(new ActivateSetAction(myKeeper), JFLAPPreferences.getDefaultTextSize());
		}
	}
}
