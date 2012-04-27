package view.util.undo;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public abstract class EditingPanel extends JPanel implements ChangeListener{

	
	private UndoKeeper myKeeper;

	public EditingPanel(boolean editable, UndoKeeper keeper){
		myKeeper = keeper;
		amEditable = editable;
	}
	
	private boolean amEditable;

	public void setEditable(boolean editable) {
		amEditable = editable;
	}

	public boolean isEditable() {
		return amEditable;
	}
	

	public UndoKeeper getKeeper(){
		return myKeeper;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		this.updateAndRepaint();
	}

	private void updateAndRepaint() {
		this.update();
		this.repaint();
	}

	public abstract void update();
}
