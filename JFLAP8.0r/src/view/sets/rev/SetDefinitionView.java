package view.sets.rev;

import model.sets.AbstractSet;
import model.undo.UndoKeeper;
import util.view.magnify.MagnifiablePanel;
import view.sets.PropertiesPanel;

public class SetDefinitionView extends MagnifiablePanel {

	private AbstractSet mySet;
	private UndoKeeper myKeeper;
	
	public SetDefinitionView(AbstractSet set, UndoKeeper keeper) {
		mySet = set;
		myKeeper = keeper;
		
		add(new PropertiesPanel(mySet));
	}
	
	
}
