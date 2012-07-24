package model.sets.num;

import java.util.Set;

import model.sets.AbstractSet;
import model.sets.PredefinedNumberSet;
import model.sets.elements.Element;

public class FactorSet extends PredefinedNumberSet {
	
	private int myFactor;
	
	public FactorSet () {
		
	}

	@Override
	public AbstractSet getNumbersInRange(int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getNthElement(int n) {
		// TODO Auto-generated method stub
		return new Element(n * myFactor);
	}

	@Override
	public void generateMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Element> getSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(Element e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSetAsString() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
