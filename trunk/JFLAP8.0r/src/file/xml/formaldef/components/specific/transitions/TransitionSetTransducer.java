package file.xml.formaldef.components.specific.transitions;

import model.automata.Transition;
import model.automata.TransitionSet;
import model.formaldef.components.SetComponent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.Copyable;
import file.xml.formaldef.components.FunctionSetTransducer;
import file.xml.formaldef.components.FunctionTransducer;

public class TransitionSetTransducer<T extends Transition> extends FunctionSetTransducer<T> {

	public TransitionSetTransducer(FunctionTransducer<T> trans) {
		super(trans);
	}

	@Override
	public String getTag() {
		return TRANS_SET;
	}

	@Override
	public SetComponent<T> createEmptyComponent() {
		return new TransitionSet<T>();
	}


}
