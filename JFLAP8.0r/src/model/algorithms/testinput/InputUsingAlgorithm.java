package model.algorithms.testinput;

import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.testinput.parse.ParserException;
import model.formaldef.FormalDefinition;
import model.symbols.SymbolString;
import errors.BooleanWrapper;

public abstract class InputUsingAlgorithm<T extends FormalDefinition> extends FormalDefinitionAlgorithm<T> {

	private SymbolString myInput;
	
	public InputUsingAlgorithm(T fd) {
		super(fd);
	}

	public void setInput(SymbolString string){
		BooleanWrapper isValid = isValidInput(string);
		if (isValid.isError())
			throw new ParserException(isValid.getMessage());
		myInput = string;
	}

	public BooleanWrapper isValidInput(SymbolString string){
		if (string == null) return new BooleanWrapper(true);
		return checkValid(string);
	}
	
	public abstract BooleanWrapper checkValid(SymbolString strin);
	
	public SymbolString getInput(){
		return myInput;
	}
	
	public boolean hasInput(){
		return myInput != null;
	}
	
	@Override
	public boolean reset() throws AlgorithmException {
		setInput(null);
		return true;
	}
}
