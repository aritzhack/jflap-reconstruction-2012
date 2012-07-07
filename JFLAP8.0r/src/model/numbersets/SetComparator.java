package model.numbersets;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetComparator implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	private boolean isInteger (Object o) {
		try {
			int i = Integer.parseInt(o.toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private boolean isNumberSet (Object o) {
		
		return true;
	}
	

	public static void main (String[] args) {
		
		String s = "([0-9]+)+";

		s = "[(\\s+)|(\\s*,\\s*)]";
				
		String input = "{1, 2, {3, 4}}";
		Pattern p = Pattern.compile(s);
		Matcher mat = p.matcher(input);
		
		
		
		boolean m = mat.matches();
		System.out.println(mat.matches());
		
		if (m) {
			String[] array = input.split(s);
			for (String a : array) {
				System.out.print(s + "  ");
			}
			System.out.println("/");
		}
	}
	
}
