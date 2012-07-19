package view.sets;

/**
 * Shows list of active sets,
 * panel of operation buttons,
 * and button to make new set
 */


import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import model.undo.UndoKeeper;
import util.view.magnify.Magnifiable;
import util.view.magnify.MagnifiablePanel;

@SuppressWarnings("serial")
public class DefaultSetPanel extends MagnifiablePanel {
	
	private UndoKeeper myKeeper;
	
	public DefaultSetPanel (UndoKeeper keeper, SetsView parent) {
		myKeeper = keeper;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setSize(parent.getWidth()/2, parent.getHeight());
		
		NewSetButton create = new NewSetButton(myKeeper);
		create.setAlignmentX(CENTER_ALIGNMENT);
		
		this.add(new SetOperationsPanel());
		this.add(parent.getActiveSetDisplay());
		this.add(create);
		
	}

	
}
