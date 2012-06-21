package model.automata.turing.universal;

import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.conversion.ConversionAlgorithm;
import model.automata.turing.MultiTapeTuringMachine;
import model.formaldef.FormalDefinition;
import model.formaldef.components.symbols.SymbolString;

public class TMtoEncodingConversion extends FormalDefinitionAlgorithm<MultiTapeTuringMachine> {

	public TMtoEncodingConversion(MultiTapeTuringMachine fd) {
		super(fd);
	}

	@Override
	public String getDescriptionName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(MultiTapeTuringMachine fd) {
		
		if (fd.getNumTapes() != 1)
			return new BooleanWrapper[]{new BooleanWrapper(false, "The " +
					"Turing machine must be single tape to be converted into a " +
					"Universal TM encoding. ")};
		return new BooleanWrapper[0];
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean reset() throws AlgorithmException {
		// TODO Auto-generated method stub
		return false;
	}

	public SymbolString getEncoding() {
		
		return null;
	}


}
