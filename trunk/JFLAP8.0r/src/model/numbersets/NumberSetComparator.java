package model.numbersets;

/**
 * @author peggyli
 */

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class NumberSetComparator implements Comparator<Set<Integer>> {

	@Override
	public int compare(Set<Integer> a, Set<Integer> b) {
		if (a.size() != b.size())
			return a.size() - b.size();

		Iterator<Integer> itera = a.iterator(), iterb = b.iterator();
		while (itera.hasNext() && iterb.hasNext()) {
			if (itera.next() != iterb.next()) {
				return itera.next() - iterb.next();
			}
		}
		
		return 0;
	}

}
