package model.sets.num;

import java.util.Set;

import model.sets.AbstractSet;
import model.sets.elements.Element;

public class CongruenceSet extends PredefinedNumberSet {
	
	/**
	 * Constructor for all numbers i such that i mod modulus = startValue mod modulus
	 * 
	 * @param modulus
	 *            the modulus of the congruence relation
	 * @param startValue
	 *            first value in the set
	 */
	public CongruenceSet (int modulus, int start) {
		
	}
	

	@Override
	public AbstractSet getNumbersInRange(int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getNthElement(int n) {
		// TODO Auto-generated method stub
		return null;
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
		return "Congruence Set";
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
