package model.sets.operations;

import model.sets.AbstractSet;
import model.sets.CustomFiniteSet;
import model.sets.CustomInfiniteSet;
import model.sets.elements.Element;
import debug.JFLAPDebug;

public class Intersection extends SetOperation {

	public Intersection () {
		super();
	}

	@Override
	public AbstractSet evaluate() {
		
		CustomFiniteSet answer = new CustomFiniteSet();
		
//		if (!myOperands.get(0).isFinite() || !myOperands.get(1).isFinite()) {
//			answer = new CustomInfiniteSet();
//		}		
		
		if (answer.isFinite()) {
			// case 1 - both sets are finite
			for (Element e : myOperands.get(0).getSet()) {
				if (myOperands.get(1).getSet().contains(e)) {
					((CustomFiniteSet) answer).add(e);
				}
			}
			
		}
		else {
			
		}
				
		answer.setName(getDescription());
		
		return answer;
	}

	@Override
	public int getNumberOfOperands() {
		return 2;
	}

	@Override
	public String getName() {
		return "Intersection";
	}

	@Override
	public String getDescription() {
		return "The intersection of " + myOperands.get(0).getName() 
				+ " and " + myOperands.get(1).getName();
	}

}
