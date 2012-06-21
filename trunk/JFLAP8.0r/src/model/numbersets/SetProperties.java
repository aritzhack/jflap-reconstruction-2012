package model.numbersets;

/**
 * @author Peggy Li
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SetProperties {

	public static Set<Integer> union(Set<Integer> first, Set<Integer> second) {
		Set<Integer> union = new TreeSet<Integer>(first);
		union.addAll(second);
		return union;
	}

	public static Set<Integer> intersection(Set<Integer> first,
			Set<Integer> second) {
		Set<Integer> intersect = new TreeSet<Integer>();
		for (int i : first) {
			if (second.contains(i))
				intersect.add(i);
		}
		return intersect;
	}

	public static Set<Integer> difference(Set<Integer> first,
			Set<Integer> second) {
		Set<Integer> diff = new TreeSet<Integer>();
		for (int i : first) {
			if (!second.contains(i))
				diff.add(i);
		}
		return diff;
	}

	/**
	 * Adapted algorithmic implementation of the recursive Java solution:
	 * http://rosettacode.org/wiki/Power_set#Java
	 * 
	 * @param set
	 * @return
	 */
	public static Set<Set<Integer>> powerset(Set<Integer> set) {
		Comparator comp = (Comparator) new NumberSetComparator();
		Set<Set<Integer>> powerset = new TreeSet<Set<Integer>>(comp);
		powerset.add(new TreeSet<Integer>());

		if (set.isEmpty())
			return powerset;

		ArrayList<Integer> list = new ArrayList<Integer>(set);
		Integer i = list.get(0);
		List<Integer> sub = list.subList(1, list.size());
		for (Set<Integer> s : powerset(new TreeSet<Integer>(sub))) {
			Set<Integer> temp = new TreeSet<Integer>();
			temp.add(i);
			temp.addAll(s);
			powerset.add(temp);
			powerset.add(s);
		}

		return powerset;
	}


}
