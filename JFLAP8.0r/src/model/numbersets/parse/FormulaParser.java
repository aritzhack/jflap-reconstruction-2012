package model.numbersets.parse;

public class FormulaParser {

	private String input;
	private int index;
	
	public FormulaParser (String input) {
		this.input = input.trim();
		this.index = 0;
	}
	
	
	public void parse () {
		// TODO
	}
	
	
	public void skipWhitespace () {
		while (Character.isWhitespace(getCharAtIndex(index)) && index < input.length()) {
			index++;
		}
	}
	
	
	public char getCharAtIndex (int index) {
		return input.charAt(index);
	}
	
	
	public String getStringFromIndex (int index) {
		return input.substring(index);
	}
}
