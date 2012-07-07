package model.numbersets.operations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import model.numbersets.AbstractNumberSet;
import model.numbersets.NumberSetComparator;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Union extends SetOperation {
	
	
	public Union (ArrayList<AbstractNumberSet> operands) {
		super(operands);
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
	public AbstractNumberSet evaluate() {
		
		
		Set<Integer> a = (Set<Integer>) myOperands.get(0);
		Set<Integer> b = (Set<Integer>) myOperands.get(1);
		
		Comparator comp = new NumberSetComparator();
		Set answer = new TreeSet<Integer>(comp);
		answer.addAll(a);
		answer.addAll(b);
		
		return null;
	}

	

}
