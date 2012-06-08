package file.xml.formaldef.components.specific.states;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.automata.State;
import file.xml.XMLTransducer;
import file.xml.XMLHelper;

public class StateTransducer implements XMLTransducer<State> {

	private static final String ID_TAG = "id";
	private static final String NAME_TAG = "name";
	private static final String STATE_TAG = "state";
	
	@Override
	public State fromStructureRoot(Element root) {
		Element id_ele = XMLHelper.getChildWithTag(root, ID_TAG);
		Element name_ele = XMLHelper.getChildWithTag(root, NAME_TAG);
		
		int id = Integer.valueOf(XMLHelper.containedText(id_ele));
		String name = XMLHelper.containedText(name_ele);
		return new State(name,id);
	}

	@Override
	public Element toXMLTree(Document doc, State item) {
		Element parent = XMLHelper.createElement(doc, STATE_TAG, null, null);
		Element id = XMLHelper.createElement(doc, ID_TAG, item.getID(), null);
		Element name = XMLHelper.createElement(doc, NAME_TAG, item.getName(), null);
		parent.appendChild(name);
		parent.appendChild(id);
		return parent;
	}

	@Override
	public String getTag() {
		return STATE_TAG;
	}

}
