package test;

import java.io.File;

public class LSystemFileTester {

	public static void main(String[] args){
		String toSave = System.getProperties().getProperty("user.dir")
				+ "/filetest";
		
		File f = new File(toSave + "/lsystem.jff");
		
	}
	
	
}
