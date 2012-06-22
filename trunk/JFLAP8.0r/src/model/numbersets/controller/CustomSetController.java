package model.numbersets.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.TimeZone;

import model.numbersets.CustomSet;


public class CustomSetController {
	
	private CustomSet myCustomSet;
	
	private static final String DELIMITER = "[\\s|,]+";
	
	public CustomSetController() {
		
		myCustomSet = new CustomSet();
	}
	
	
	public void parseAndAddElements (String input) {
		String[] array = input.split(DELIMITER);
	
		for (String s : array) {
			try {
				int i = Integer.parseInt(s);
				myCustomSet.add(i);
			} catch (NumberFormatException e) {
				
			}
		}
		
	}
	
	
	public Set<Integer> getElements () {
		return myCustomSet.getSet();
	}
	
	
	/**
	 * Default name assigned to the set if the user does not provide one
	 * 	formed by concatenating the user name with system date and date
	 * 	to guarantee a unique name
	 * 
	 * @return
	 */
	private String generateDefaultName () {
		String name = System.getProperty("user.name");
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		return name + format.format(cal.getTime());
	}
	

}
