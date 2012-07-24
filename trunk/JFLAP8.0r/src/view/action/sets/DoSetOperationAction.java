package view.action.sets;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;

import model.sets.AbstractSet;
import model.sets.SetsManager;
import model.sets.operations.SetOperation;
import model.sets.operations.SetOperationException;

public class DoSetOperationAction extends AbstractAction {

	private SetOperation myOperation;

	public DoSetOperationAction(SetOperation operation) {
		super(operation.getName());
		myOperation = operation;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		ArrayList<AbstractSet> operands = SetsManager.ACTIVE_REGISTRY.getSelectedSets();
		try {
			myOperation.setOperands(operands);
			AbstractSet answer = myOperation.evaluate();
			SetsManager.ACTIVE_REGISTRY.add(answer);
		} catch (SetOperationException e1) {
			e1.printStackTrace();
		}
	}
	
}
