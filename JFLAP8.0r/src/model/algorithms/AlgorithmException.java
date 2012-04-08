package model.algorithms;

import errors.BooleanWrapper;

public class AlgorithmException extends RuntimeException {

	public AlgorithmException() {
		super();
	}

	public AlgorithmException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AlgorithmException(String arg0) {
		super(arg0);
	}

	public AlgorithmException(Throwable arg0) {
		super(arg0);
	}

	public AlgorithmException(BooleanWrapper ... bw) {
		super(BooleanWrapper.createErrorLog(bw));
	}

}
