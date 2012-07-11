package view.numsets;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import model.sets.CustomSet;


/**
 * Uses reflection-based techniques to map strings to classes, instantiate, etc.
 * @author peggyli
 *
 */

public class SetInitiationHelper {
	
	private static final String DELIMITERS = "[\\s|,]+";

	private CustomSet mySet;
	
	public SetInitiationHelper () {
		mySet = new CustomSet();
	}
	
	
	
	public Set<Integer> parse (String input) {
		String[] elements = input.split(DELIMITERS);
		Set<Integer> set = new TreeSet<Integer>();
		for (String s : elements) {
			set.add(Integer.parseInt(s));
		}
		return set;
	}
	

	
}
