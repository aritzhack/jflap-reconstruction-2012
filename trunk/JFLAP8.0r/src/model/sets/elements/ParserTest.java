package model.sets.elements;

public class ParserTest {

	
	public static void main(String[] args) {
		
		String input = "12 34    98 5";
		ElementsParser parser = new ElementsParser(input);
		try {
			System.out.println(parser.parse());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
