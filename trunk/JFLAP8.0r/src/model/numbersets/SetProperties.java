package model.numbersets;

import java.util.Set;
import java.util.TreeSet;

public class SetProperties {

	public static Set<Integer> union (Set<Integer> first, Set<Integer> second) {
		Set<Integer> union = new TreeSet<Integer>(first);
		union.addAll(second);
		return union;
	}
	
	public static Set<Integer> intersection (Set<Integer> first, Set<Integer> second) {
		Set<Integer> intersect = new TreeSet<Integer>();
		for (int i : first) {
			if (second.contains(i))
				intersect.add(i);
		}
		return intersect;
	}
	
}
