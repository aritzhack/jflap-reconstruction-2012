package test;

import javax.swing.JOptionPane;

import debug.JFLAPDebug;

import errors.JFLAPError;
import errors.JFLAPException;

public class RunTests {

	public static void main(String[] args) {
		try{
		new RegExTest();
		new FSATest();
		}catch(Exception e){
			if (e instanceof JFLAPException)
				showMessage((JFLAPException) e);
			else
				e.printStackTrace();
		}
	}

	private static void showMessage(JFLAPException e) {
		JFLAPDebug.print("Huh?");
		JFLAPError.show(e.getMessage(), "UHOH!");
	}
}
