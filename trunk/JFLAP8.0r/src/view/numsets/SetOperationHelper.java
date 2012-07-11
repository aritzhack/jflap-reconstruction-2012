package view.numsets;

import java.util.ArrayList;
import java.util.Arrays;

import errors.BooleanWrapper;

import model.numbersets.operations.SetOperation;
import model.sets.AbstractNumberSet;

public class SetOperationHelper {

	private SetOperation myOperation;
	private ArrayList<AbstractNumberSet> myOperands;

	public SetOperationHelper(SetOperation operation, AbstractNumberSet[] selected) {
		myOperation = operation;
		myOperands = (ArrayList<AbstractNumberSet>) Arrays.asList(selected);
		
		if (checkOperands().isTrue()) {
			myOperation.evaluate();
		}

	}

	
	private boolean isFinite () {
		for (AbstractNumberSet op : myOperands) {
			if (op.isFinite())
				return true;
		}
		return false;
	}
	
	
	private BooleanWrapper checkOperands() {
		if (myOperands.size() != myOperation.getNumberOfOperands()) {
			return new BooleanWrapper(false, 
					String.format("%s takes %d operands but you gave %d.",
					myOperation.getName(), 
					myOperation.getNumberOfOperands(),
					myOperands.size()));
		}
		return new BooleanWrapper(true);
	}
}
