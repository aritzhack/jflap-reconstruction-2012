package file.xml.formaldef.lsystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.symbols.Symbol;
import model.symbols.SymbolString;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.BasicTransducer;
import file.xml.TransducerFactory;
import file.xml.XMLHelper;
import file.xml.XMLTransducer;
import file.xml.formaldef.components.symbols.SymbolStringTransducer;

public class ParameterTransducer extends BasicTransducer<Parameter> {

	@Override
	public String getTag() {
		return PARAMETER_TAG;
	}

	@Override
	public Parameter fromStructureRoot(Element root) {
		List<Element> eleChildren = XMLHelper.getElementChildren(root);
		Map<String, String> childrenMap = new HashMap<String, String>();
		for (Element e : eleChildren) {
			String tag = e.getTagName();
			XMLTransducer trans = TransducerFactory.getTransducerForTag(tag);

			childrenMap.put(tag, (String) trans.fromStructureRoot(e));
		}
		if (!childrenMap.containsKey(PARAMETER_NAME_TAG)
				|| !childrenMap.containsKey(PARAMETER_VALUE_TAG))
			return new Parameter(null, "");
		String name = childrenMap.get(PARAMETER_NAME_TAG), value = childrenMap
				.get(PARAMETER_VALUE_TAG);

		return new Parameter(name, value);
	}

	@Override
	public Element toXMLTree(Document doc, Parameter param) {

		Element root = XMLHelper.createElement(doc, getTag(), null, null);

		XMLTransducer trans = new SingleStringTransducer(PARAMETER_NAME_TAG);
		root.appendChild(trans.toXMLTree(doc, param.getName()));
		
		trans = new SingleStringTransducer(PARAMETER_VALUE_TAG);
		root.appendChild(trans.toXMLTree(doc, param.getValue()));

		return root;
	}

}
