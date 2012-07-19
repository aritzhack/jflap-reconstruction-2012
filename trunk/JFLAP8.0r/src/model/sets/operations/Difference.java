package model.sets.operations;

import model.sets.AbstractSet;
import model.sets.CustomFiniteSet;
import model.sets.elements.Element;

public class Difference extends SetOperation {

	@Override
	public AbstractSet evaluate() {
		// TODO infinite

		if (myOperands.get(0).isFinite() && myOperands.get(1).isFinite()) {
			CustomFiniteSet answer = new CustomFiniteSet();
			for (Element e : myOperands.get(0).getSet()) {
				if (!myOperands.get(1).contains(e))
					answer.add(e);
			}
			return answer;
		}

		
		
		return null;
	}

	@Override
	public int getNumberOfOperands() {
		return 2;
	}

	@Override
	public String getName() {
		return "Difference";
	}

	@Override
	public String getDescription() {
		return "The difference of " + myOperands.get(0).getName() +
				" and " + myOperands.get(1).getName();
	}


}
