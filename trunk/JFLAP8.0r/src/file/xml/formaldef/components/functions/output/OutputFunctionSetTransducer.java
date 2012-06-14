package file.xml.formaldef.components.functions.output;

import model.automata.transducers.OutputFunction;
import model.automata.transducers.OutputFunctionSet;
import model.formaldef.components.SetComponent;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.Copyable;

import file.xml.formaldef.components.functions.FunctionSetTransducer;
import file.xml.formaldef.components.functions.FunctionTransducer;


public class OutputFunctionSetTransducer<T extends OutputFunction> extends FunctionSetTransducer<OutputFunction> {


	public OutputFunctionSetTransducer(OutputFunctionTransducer trans) {
		super(trans);
	}

	@Override
	public String getTag() {
		return OUTPUT_FUNC_SET;
	}

	@Override
	public OutputFunctionSet createEmptyComponent() {
		return new OutputFunctionSet<T>();
	}

}
