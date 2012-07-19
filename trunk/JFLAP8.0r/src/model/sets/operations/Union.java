package model.sets.operations;

import model.sets.AbstractSet;
import model.sets.CustomFiniteSet;
import model.sets.elements.Element;

public class Union extends SetOperation {

	public Union () {

	}

	@Override
	public AbstractSet evaluate() {
		// TODO Auto-generated method stub

		if (myOperands.get(0).isFinite() && myOperands.get(0).isFinite()){
			CustomFiniteSet answer = new CustomFiniteSet();
			for (int i = 0; i < myOperands.size(); i++) {
				for (Element e : myOperands.get(i).getSet()) {
					answer.add(e);
				}
			}
			answer.setName(getDescription());
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
		return "Union";
	}

	@Override
	public String getDescription() {
		return "The union of " + myOperands.get(0).getName() + " and " 
				+ myOperands.get(1).getName();
	}

}
