package model.sets.num;

import java.util.Set;

import model.sets.AbstractSet;
import model.sets.PredefinedNumberSet;
import model.sets.elements.Element;

public class PrimesSet extends PredefinedNumberSet {

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
		return "Prime Numbers";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(Element e) {
		return isPrime(Integer.parseInt(e.getValue()));
	}
	
	
	private boolean isPrime (int n) {
		for (int i = 2; i <= Math.sqrt(n); i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

}
