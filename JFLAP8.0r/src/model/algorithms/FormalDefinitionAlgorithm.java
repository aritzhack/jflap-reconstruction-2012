package model.algorithms;

import debug.JFLAPDebug;
import errors.BooleanWrapper;
import model.formaldef.FormalDefinition;


public abstract class FormalDefinitionAlgorithm<T extends FormalDefinition> extends SteppableAlgorithm {

	private T myFormalDefinition;

	public FormalDefinitionAlgorithm(T fd) {
		myFormalDefinition = fd;

		BooleanWrapper[] bw = fd.isComplete();
		if (bw.length > 0){
			throw new AlgorithmException(bw);
		}
		bw = checkOfProperForm(fd);

		if (bw.length > 0)
			throw new AlgorithmException(bw);
		if (!this.reset())
			throw new AlgorithmException("There an error occured with the initialization " +
											"of the " + this.getDescriptionName() + ".");
	}
	
	/**
	 * Checks to see if the automaton to be converted is
	 * of the proper form for this algorithm. <code>isComplete()</code>
	 * has already been checked in the constructor.
	 * 
	 * @param fd
	 * @return
	 */
	public abstract BooleanWrapper[] checkOfProperForm(T fd);

	/**
	 * Returns the formal definition object that this definition
	 * was initialized with.
	 * 
	 * @return
	 */
	public T getOriginalDefinition(){
		return myFormalDefinition;
	}

}
