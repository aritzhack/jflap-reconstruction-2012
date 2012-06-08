package file.xml.formaldef.components.specific.transitions;

import model.automata.Transition;
import model.automata.TransitionSet;
import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.Alphabet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.formaldef.components.SetComponentTransducer;

import util.Copyable;

public class TransitionSetTransducer<T extends Transition> extends SetComponentTransducer<T> {

	
	private TransitionTransducer<T> mySubTransducer;

	public TransitionSetTransducer(TransitionTransducer<T> trans){
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
	public SetComponent<T> createEmptyComponent() {
		return new TransitionSet<T>();
	}

	@Override
	public Element createSubNode(Document doc, T item) {
		return mySubTransducer.toXMLTree(doc, item);
	}

	@Override
	public String getTag() {
		return "transition_set";
	}


}
