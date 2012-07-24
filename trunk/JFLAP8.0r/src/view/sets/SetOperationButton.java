package view.sets;

import model.sets.operations.SetOperation;
import util.view.magnify.MagnifiableButton;
import view.action.sets.DoSetOperationAction;

@SuppressWarnings("serial")
public class SetOperationButton extends MagnifiableButton {
	
	
	public SetOperationButton (SetOperation operation) {
		super(new DoSetOperationAction(operation), 25);
		setToolTipText(createToolTipText(operation));
	}
	
	private String createToolTipText(SetOperation op) {
		return op.getName() + " (" + op.getNumberOfOperands() + ")";
	}

}
