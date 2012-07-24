package model.sets.num;

import java.util.Set;
import java.util.TreeSet;

import model.sets.AbstractSet;
import model.sets.CustomFiniteSet;
import model.sets.elements.Element;

public class EvensSet extends PredefinedNumberSet {
	
	private Set<Element> myElements;
	
	public EvensSet () {
		myElements = new TreeSet<Element>();
	}

	@Override
	public AbstractSet getNumbersInRange(int min, int max) {
		Set<Element> values = new TreeSet<Element>();
		
		min = min % 2 == 0 ? min : min + 1;
		for (int i = min/2; i <= max/2; i++) {
			values.add(new Element(i * 2));
		}
		
		CustomFiniteSet answer = new CustomFiniteSet(values);
		answer.setName("Even numbers between " + min + " and " + max);
		return answer;
	}

	@Override
	public Element getNthElement(int n) {
		return new Element(2 * n);
	}

	@Override
	public void generateMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Element> getSet() {
		return myElements;
	}

	@Override
	public String getName() {
		return "Even numbers";
	}

	@Override
	public String getDescription() {
		return "The set of non-negative even integers";
	}

	@Override
	public boolean contains(Element e) {
		return ((Integer.parseInt(e.getValue()))%2 == 0);
	}

	@Override
	public String getSetAsString() {
		return super.getSetAsString();
	}

}
