package model.numbersets.sets;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElementParser {
	
	/*
	 * Assumes elements are separated by only whitespace,
	 * or by a comma possibly with leading/trailing whitespace
	 */
	private static final Pattern OPENING = Pattern.compile("\\{");
	private static final Pattern CLOSING = Pattern.compile("\\}");
	
	private static final String OPEN = "{";
	private static final String CLOSE = "}";
	
	private String myInput;
	private int myIndex;
	private boolean isBalanced;
	private Stack<String> myStack;
	private ArrayList<String> myElements;
	
	
	public ElementParser (String input) {
		myInput = input;
		myIndex = 0;
		isBalanced = true;
		
		myStack = new Stack<String>();
		myElements = new ArrayList<String>();
	}
	
	
	public ArrayList<String> parse () throws Exception {
		
		// keeps track of nested sets, e.g. {1, 2} in {{1, 2}, 3}
		String running = "";
		
		while (myIndex < myInput.length()) {
			int stop = getIndexOfNextDelimiter();
			System.out.println("Current index = " + myIndex + "; Next delimiter at index " + stop);
			
			String str = myInput.substring(myIndex, stop);
			System.out.println("Substring is: " + str);
			
			if (stop > myInput.length()) 
				break;
			
			running += str;
			
			Matcher matchOpen = OPENING.matcher(str);
			boolean isStart = matchOpen.find();
			
			if (isStart) {
				String openSymbol = str.substring(matchOpen.start(), matchOpen.start() + 1);
				myStack.push(openSymbol);
				
				System.out.println("Opening " + openSymbol + " found" );
				System.out.println("Running string is " + running);
				System.out.println("Stack is now: " + myStack);
			}
			
			
			Matcher matchClose = CLOSING.matcher(str);
			boolean isFinish = matchClose.find();
			
			
			if (isFinish) {
				String match = myStack.pop();
//				if (OPEN.indexOf(match) != CLOSE.indexOf(str)) {
//					throw new Exception("Invalid format");
//				}
				System.out.println("Closing " + str + " found");
			}
						
			isBalanced = myStack.size() == 0;

			if (isBalanced) {
				myElements.add(running);			
				running = "";
				advancePastDelimiters();
			}
			
			myIndex = stop;
			
			System.out.println();
		}
		return myElements;
	}
	
	
	private char currentChar () {
		return myInput.charAt(myIndex);
	}
	
	
	private boolean isDelimiter (char c) {
		return Character.isWhitespace(c) || c == ',';
	}
	
	private int getIndexOfNextDelimiter () {
		int start = myIndex;
		while (myIndex < myInput.length() && !isDelimiter(currentChar())) {
//			System.out.println(myIndex);
			myIndex++;
		}
		
		int temp = start;
		start = myIndex;
		myIndex = temp;
		
		return start;
	}
	
	
	private void advancePastDelimiters () {
		while (myIndex < myInput.length() && isDelimiter(currentChar())) {
			myIndex++;
		}
	}
	
	
	public static void main (String[] args) throws Exception {
//		String input = "1 , {2}";
		String input = "{{{3}}}";
		ElementParser parser = new ElementParser(input);
		ArrayList<String> ans = parser.parse();
		System.out.println("\n\n");
		for (String s : ans) {
			System.out.println(s);
		}
		System.out.println(ans.size() + " elements parsed");		
	}

}
