package view.sets;

/**
 * Shows list of active sets,
 * panel of operation buttons,
 * and button to make new set
 */


import javax.swing.BoxLayout;

import model.undo.UndoKeeper;
import util.view.magnify.MagnifiablePanel;

@SuppressWarnings("serial")
public class DefaultSetPanel extends MagnifiablePanel {
	
	private UndoKeeper myKeeper;
	
	public DefaultSetPanel (UndoKeeper keeper, SetsView parent) {
		myKeeper = keeper;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		NewSetButton create = new NewSetButton(myKeeper);
		
		this.add(new SetOperationsPanel());
		this.add(parent.getActiveSetDisplay());
		this.add(create);
		create.setAlignmentX(CENTER_ALIGNMENT);
		
	}

	
}
