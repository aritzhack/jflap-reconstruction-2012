package oldnewstuff.view;

import javax.swing.JPanel;

import model.undo.UndoKeeper;

public abstract class EditingPanel extends JPanel{

	private UndoKeeper myKeeper;
	private boolean amEditable;
	
	public EditingPanel(UndoKeeper keeper, boolean editable){
		myKeeper = keeper;
		amEditable = editable;
	}
	
	public UndoKeeper getKeeper() {
		return myKeeper;
	}
	public boolean isEditable(){
		return amEditable;
	}
	
	public void setEditable(boolean editable){
		amEditable = editable;
	}
	
}
