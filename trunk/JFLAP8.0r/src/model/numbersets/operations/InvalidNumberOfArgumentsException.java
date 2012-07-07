package model.numbersets.operations;

@SuppressWarnings("serial")
public class InvalidNumberOfArgumentsException extends RuntimeException {
	
	public InvalidNumberOfArgumentsException () {
		super();
	}
	
	public InvalidNumberOfArgumentsException (String arg0) {
		super(arg0);
	}
	
	public InvalidNumberOfArgumentsException (Throwable arg0) {
		super(arg0);
	}
	
	public InvalidNumberOfArgumentsException (String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
