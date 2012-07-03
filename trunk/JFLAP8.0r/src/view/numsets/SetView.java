package view.numsets;

import java.awt.BorderLayout;

import model.numbersets.control.SetsManager;
import model.undo.UndoKeeper;
import view.EditingPanel;

@SuppressWarnings("serial")
public class SetView extends EditingPanel {

	public SetView (SetsManager manager) {
		super(new UndoKeeper(), true);
		this.setLayout(new BorderLayout());
		
		this.add(new ActiveSetDisplay(), BorderLayout.WEST);
		this.add(new SetInitiationPanel(), BorderLayout.SOUTH);
		this.add(new OperationsPanel(), BorderLayout.NORTH);
		
		
	}
	
}
