package model.numbersets.control;

public class InterpreterTest {

	public static void main (String[] args) {
		String input = "1, 2, {3, 4}, 5, {}";
		
		SetInterpreter inter = new SetInterpreter(input);
		
		System.out.println(inter.parse());
	
//		System.out.println(inter.isDelimiter(','));
		
	}
	
}
