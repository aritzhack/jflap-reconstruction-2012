package util;

import javax.swing.Action;
import javax.swing.JButton;

public class ActionLinkedButton extends JButton{

	private Action myAction;

	public ActionLinkedButton(Action action) {
		super(action);
		myAction = action;
		this.repaint();
	}


	public void update() {
		if (myAction != null && myAction.isEnabled() != this.isEnabled()){
			super.setEnabled(myAction.isEnabled());
		}
	}
	
	
}