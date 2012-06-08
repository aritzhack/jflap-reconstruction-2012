package file.xml.formaldef.components;

import model.automata.Transition;
import model.automata.TransitionSet;
import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.functionset.function.LanguageFunction;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


import util.Copyable;

public abstract class FunctionSetTransducer<T extends LanguageFunction> extends SetComponentTransducer<T> {

	
	private FunctionTransducer<T> mySubTransducer;

	public FunctionSetTransducer(FunctionTransducer<T> trans){
		mySubTransducer=trans;
	}
	
	@Override
	public String getSubNodeTag() {
		return mySubTransducer.getTag();
	}

	@Override
	public T decodeSubNode(Element item) {
		return mySubTransducer.fromStructureRoot(item);
	}

	@Override
	public Element createSubNode(Document doc, T item) {
		return mySubTransducer.toXMLTree(doc, item);
	}


}