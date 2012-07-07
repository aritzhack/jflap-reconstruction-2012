package model.numbersets.controller;

import java.util.Set;

import model.numbersets.AbstractNumberSet;
import model.numbersets.operations.SetOperation;

public class OperationsController {
	
	private SetOperation myOperation;
	private Set<AbstractNumberSet> mySets;
	
	
	public OperationsController (SetOperation operation, Set<AbstractNumberSet> sets) {
		myOperation = operation;
		mySets = sets;
		
	}
	
	
//	public Set doOperation () throws Exception {
//		if (mySets.size() != myOperation.getNumberOfOperands()) {
//			throw new Exception("Invalid number of arguments");
//		}
//		return myOperation.doOperation(mySets);
//	}
	
	
	public SetOperation getOperation () {
		return myOperation;
	}
	
	public Set<AbstractNumberSet> mySets () {
		return mySets;
	}
}
