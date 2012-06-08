package file.xml.formaldef.components.specific.states;

import java.util.SortedSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import errors.BooleanWrapper;
import file.xml.XMLHelper;
import file.xml.formaldef.components.SetComponentTransducer;
import model.automata.State;
import model.automata.StateSet;
import model.formaldef.components.SetComponent;
import model.formaldef.components.symbols.Symbol;

public class StateSetTransducer extends SetComponentTransducer<State> {

	private StateTransducer myStateTransducer;

	public StateSetTransducer() {
		myStateTransducer = new StateTransducer();
	}
	
	@Override
	public State decodeSubNode(Element item) {
		return myStateTransducer.fromStructureRoot(item);
	}

	@Override
	public StateSet createEmptyComponent() {
		return new StateSet();
	}

	@Override
	public String getSubNodeTag() {
		return myStateTransducer.getTag();
	}

	@Override
	public Element createSubNode(Document doc, State item) {
		return myStateTransducer.toXMLTree(doc, item);
	}

	@Override
	public String getTypeTag() {
		return STATE_SET_TAG;
	}


}
