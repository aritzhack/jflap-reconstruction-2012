package file.xml.formaldef.components;

import model.formaldef.components.symbols.SpecialSymbol;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import file.xml.StructureTransducer;
import file.xml.XMLHelper;

public abstract class SingleValueTransducer<T> extends StructureTransducer<T> {

	private static final String VALUE_TAG = "value";

	@Override
	public T fromStructureRoot(Element root) {
		NodeList list = root.getElementsByTagName(VALUE_TAG);
		String s = XMLHelper.containedText(list.item(0));
		return this.createInstance(s);
	}

	public abstract T createInstance(String s);

	@Override
	public Element appendComponentsToRoot(Document doc, T structure, Element root) {
		Element e = XMLHelper.createElement(doc, VALUE_TAG, retrieveData(structure), null);
		root.appendChild(e);
		return root;
	}

	public abstract Object retrieveData(T structure);



}
