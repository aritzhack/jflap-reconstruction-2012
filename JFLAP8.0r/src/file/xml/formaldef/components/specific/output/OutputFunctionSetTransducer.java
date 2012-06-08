package file.xml.formaldef.components.specific.output;

import model.automata.transducers.OutputFunction;
import model.automata.transducers.OutputFunctionSet;
import model.formaldef.components.SetComponent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.Copyable;
import file.xml.formaldef.components.FunctionSetTransducer;
import file.xml.formaldef.components.FunctionTransducer;

public class OutputFunctionSetTransducer<T extends OutputFunction> extends FunctionSetTransducer<T> {


	public OutputFunctionSetTransducer(FunctionTransducer<T> trans) {
		super(trans);
	}

	@Override
	public String getTypeTag() {
		return OUTPUT_FUNC_SET;
	}

	@Override
	public SetComponent<T> createEmptyComponent() {
		return new OutputFunctionSet<T>();
	}

}
