package view.sets.edit;

import java.awt.GridLayout;

import model.undo.UndoKeeper;
import util.view.magnify.MagnifiablePanel;
import view.sets.SetsDropdownMenu;

public class EditingOptions extends MagnifiablePanel {

	private UndoKeeper myKeeper;
	private SetsDropdownMenu myDropdownMenu;
	
	public EditingOptions(UndoKeeper keeper) {
		myKeeper = keeper;
		
		setLayout(new GridLayout(0, 1));
		
		
	}
	
}
