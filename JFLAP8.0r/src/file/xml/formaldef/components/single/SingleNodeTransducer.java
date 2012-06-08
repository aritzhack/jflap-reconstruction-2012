package file.xml.formaldef.components.single;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.Transducer;
import file.xml.XMLHelper;

public abstract class SingleNodeTransducer<T> implements Transducer<T>{


	@Override
	public T fromStructureRoot(Element root) {
		String text = XMLHelper.containedText(root);
		return createInstance(text);
	}

	@Override
	public Element toXMLTree(Document doc, T structure) {
		Element e = XMLHelper.createElement(doc, getTag(), extractData(structure), null);
		return e;
	}

	public abstract Object extractData(T structure);

	public abstract T createInstance(String text);


}
