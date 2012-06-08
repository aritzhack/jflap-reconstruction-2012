package file.xml.formaldef.components.specific.states;

import model.automata.StartState;
import model.automata.State;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.StructureTransducer;
import file.xml.XMLHelper;
import file.xml.formaldef.components.SingleValueTransducer;

public class StartStateTransducer extends StructureTransducer<StartState> {

	private StateTransducer myStateTransducer;

	public StartStateTransducer() {
		myStateTransducer = new StateTransducer();
	}
	
	@Override
	public StartState fromStructureRoot(Element root) {
		Element state_ele = XMLHelper.getChildWithTag(root, myStateTransducer.getTag());
		State s = myStateTransducer.fromStructureRoot(state_ele);
		return new StartState(s);
	}

	@Override
	public Element appendComponentsToRoot(Document doc, StartState structure,
			Element root) {
		State s = structure.toStateObject();
		root.appendChild(myStateTransducer.toXMLTree(doc, s));
		return root;
	}

	@Override
	public String getTag() {
		return START_STATE;
	}


}
