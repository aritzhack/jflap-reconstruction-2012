package model.numbersets.controller;

import java.util.Iterator;

import model.numbersets.AbstractNumberSet;
import model.numbersets.CustomSet;
import model.numbersets.defined.PredefinedSet;

public class SetIterator {
	
	private AbstractNumberSet setObj;
	
	private Iterator<Integer> iter;
	/**
	 * Current position of the iterator
	 */
	private int myPosition;
	
	public SetIterator(AbstractNumberSet numberset) {
		setObj = numberset;
		
		iter = numberset.getSet().iterator();
		myPosition = 0;
	}

	
	public int getNextNumber () throws Exception {
		if (!iter.hasNext()) {
			if (setObj.isFinite()) {
				throw new Exception("Reached end of finite set; no more elements");
			}
			((PredefinedSet) setObj).generateNextNumbers(PredefinedSet.DEFAULT_NUMBER_TO_ADD);
		
		}
		return iter.next();
		
	}
	
	private boolean hasNext () {
		return iter.hasNext();
	}
}
