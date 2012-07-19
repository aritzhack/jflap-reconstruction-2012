package view.sets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.sets.operations.SetOperation;
import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import view.action.sets.DoSetOperationAction;

@SuppressWarnings("serial")
public class SetOperationButton extends MagnifiableButton {
	
	private SetOperation myOperation;
	
	public SetOperationButton (SetOperation operation) {
		super(operation.getName(), (int) (JFLAPPreferences.getDefaultTextSize() * 0.5));
		myOperation = operation;
		
		addActionListener(new SetOperationListener());
	}

	
	private class SetOperationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			DoSetOperationAction action = new DoSetOperationAction(myOperation);
			action.actionPerformed(e);			
		}
		
	}
}
