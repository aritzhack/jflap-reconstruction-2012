package model.numbersets.control;

import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used to parse a user-input string
 * e.g. {1, 2, {3, 4}, 5, {}} contains
 * 5 elements: the numbers 1, 2, and 5,
 * the empty set, and the set {3, 4}
 *
 */

public class SetInterpreter {
	
	private static final Pattern DELIMITERS = Pattern.compile("\\s|,");
	
	private static final Character SET_LEFT_SYMBOL = '{',
			set_RIGHT_SYMBOL = '}';
	
	
	private Set myEntireSet;
	
	private Stack myStack;
	
	
	int myIndex;
	
	String myInput;
	
	public SetInterpreter (String input) {
		myInput = input;
		myIndex = 0;
		
		myEntireSet = new TreeSet();
		
		myStack = new Stack();
	}
	
	
	public Set parse () {
		skipDelimiters();
		
		return myEntireSet;
	}
	
	

	private boolean atEnd () {
		return myIndex >= myInput.length();
	}
	
	
	public boolean isDelimiter (char c) {
		Matcher m = DELIMITERS.matcher(Character.toString(c));
		return m.matches();
	}
	
	private void skipDelimiters () {
		while (!atEnd() && isDelimiter(myInput.charAt(myIndex))) {
			myIndex++;
		}
	}	
	
	public void reset () {
		myIndex = 0;
	}
	
	
}
