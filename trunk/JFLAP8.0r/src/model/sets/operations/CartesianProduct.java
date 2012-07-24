package model.sets.operations;

import java.util.LinkedHashSet;
import java.util.Set;

import model.sets.AbstractSet;
import model.sets.CustomFiniteSet;
import model.sets.elements.Element;

public class CartesianProduct extends SetOperation {

	@Override
	public AbstractSet evaluate() {
		// TODO Auto-generated method stub
		
		Set<Element> elements = new LinkedHashSet<Element>();
		for (Element first : myOperands.get(0).getSet()) {
			for (Element second : myOperands.get(1).getSet()) {
				elements.add(new Element(new Tuple(first, second).toString()));
			}
		}
		
		return new CustomFiniteSet(getDescription(), elements);
	}

	@Override
	public int getNumberOfOperands() {
		return 2;
	}

	@Override
	public String getName() {
		return "Cartesian Product";
	}

	@Override
	public String getDescription() {
		return "The Cartesian product of " + myOperands.get(0).getName() + " and " + myOperands.get(1).getName();
	}
	
	
	
	private class Tuple {
		
		private Element myFirst;
		private Element mySecond;
		
		public Tuple(Element arg0, Element arg1) {
			myFirst = arg0;
			mySecond = arg1;
		}
		
		public String toString() {
			return "(" + myFirst.toString() + ", " + mySecond.toString() + ")";
		}
	}

}
