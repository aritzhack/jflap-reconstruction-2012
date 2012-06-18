package model.numbersets;

/**
 * @author Peggy Li
 */

import java.util.Set;
import java.util.TreeSet;

import model.languages.Tuple;

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
	
	public static Set<Integer> difference (Set<Integer> first, Set<Integer> second) {
		Set<Integer> diff = new TreeSet<Integer>();
		for (int i : first) {
			if (!second.contains(i))
				diff.add(i);
		}
		return diff;
	}
	
	
}
