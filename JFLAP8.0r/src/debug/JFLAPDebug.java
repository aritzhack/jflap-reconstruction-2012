package debug;

import java.util.Arrays;

public class JFLAPDebug {

	public static void print(Object message, int s){
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String printout = stackTraceElements[2+s].getClassName() + "." + 
							stackTraceElements[2+s].getMethodName() + "(" +
							"Line: " + stackTraceElements[2+s].getLineNumber() + ")\n" +
							((message == null) ? "" : "Message: " + message);
		
		System.out.println(printout);
	}
	
	public static void print(Object message){
		print(message, 1);
	}
	
	public static void print(){
		print(null, 1);
	}
	
}
