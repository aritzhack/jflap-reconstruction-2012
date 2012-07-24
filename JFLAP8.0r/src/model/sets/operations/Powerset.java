package model.sets.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;

import model.languages.SetOperators;
import model.sets.AbstractSet;
import model.sets.CustomFiniteSet;
import model.sets.elements.Element;

public class Powerset extends SetOperation {

	@Override
	public AbstractSet evaluate() {
		// case 1 = finite
		if (myOperands.get(0).isFinite()) {
			return new CustomFiniteSet(getDescription(), null, powerset(myOperands.get(0).getSet()));
		}
		
		// answer is infinite...
		else {
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private Set<Element> powerset (Set<Element> original) {
		Set<Set<Element>> intermediate = SetOperators.powerSet(original);
		Set<Element> answer = new TreeSet<Element>();
		for (Set<Element> e : intermediate) {
			answer.add(new Element(getSetToString(e)));
		}
		return answer;
	}
	
	@SuppressWarnings("unchecked")
	private String getSetToString (Set<Element> set) {
		if (set.size() == 0)
			return JFLAPPreferences.EMPTY_SET;
		StringBuilder sb = new StringBuilder();
		ArrayList<Element> list = new ArrayList<Element>(set);
		Collections.sort(list);
		sb.append("{");
		for (int i = 0; i < set.size() - 1; i++) {
			sb.append(list.get(i) + ", ");
		}
		sb.append(list.get(list.size()-1) + "}");
		return sb.toString();
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
