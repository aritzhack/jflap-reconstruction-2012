package model.sets.operations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import model.sets.AbstractSet;
import model.sets.CustomFiniteSet;
import model.sets.elements.Element;

public class Powerset extends SetOperation {

	@Override
	public AbstractSet evaluate() {
		// case 1 = finite
		if (myOperands.get(0).isFinite()) {
			return new CustomFiniteSet(powersetHelper(myOperands.get(0).getSet()));
		}
		
		// answer is infinite...
		else {
			return null;
		}
	}
	
	
	private Set<Element> powersetHelper(Set<Element> set) {
		Set<Element> powerset = new TreeSet<Element>();
		
		powerset.add(new Element("{ }"));
		
		if (set.isEmpty())	return powerset;
		
		ArrayList<Element> list = new ArrayList<Element>(set);
		Element first = list.get(0);
		List<Element> sub = list.subList(1, list.size());
		for (Element e : powersetHelper(new HashSet<Element>(sub))) {
			Set<Element> temp = new TreeSet<Element>();
			temp.add(first);
			temp.add(e);
			powerset.addAll(temp);
			powerset.add(e);
		}
		
		return powerset;
	}
	
	
	

	@Override
	public int getNumberOfOperands() {
		return 1;
	}

	@Override
	public String getName() {
		return "Powerset";
	}

	@Override
	public String getDescription() {
		return "The powerset of " + myOperands.get(0).getName();
	}
	
	

}
