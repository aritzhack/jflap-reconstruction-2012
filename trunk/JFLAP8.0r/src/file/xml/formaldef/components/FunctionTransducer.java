package file.xml.formaldef.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import debug.JFLAPDebug;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.SymbolString;
import file.xml.XMLTransducer;
import file.xml.TransducerFactory;
import file.xml.XMLHelper;
import file.xml.formaldef.components.single.SymbolStringTransducer;

public abstract class FunctionTransducer<T extends LanguageFunction> implements XMLTransducer<T> {

	
	private Alphabet[] myAlphs;
	
	public FunctionTransducer(Alphabet ... alphs){
		myAlphs = alphs;
	}
	
	@Override
	public T fromStructureRoot(Element root) {
		List<Element> eleChildren = XMLHelper.getElementChildren(root);
		Map<String, Object> valueMap = new HashMap<String, Object>();
		for (Element e: eleChildren){
			String tag = e.getTagName();
			Alphabet[] alphs = 
					FunctionAlphabetFactory.discerneAlphabets(tag, myAlphs);
			XMLTransducer trans;
			if (alphs.length > 0)
				trans = new SymbolStringTransducer(tag, alphs);
			else
				trans = TransducerFactory.getTransducerForTag(tag);
			valueMap.put(tag, trans.fromStructureRoot(e));			
		}
		return createFunction(valueMap);
	}

	public abstract T createFunction(Map<String, Object> valueMap);
	
	@Override
	public Element toXMLTree(Document doc, T structure) {
		Map<String, Object> tagToValue = createTagToValueMap(structure);
		Element root = XMLHelper.createElement(doc, getTag(), null, null);
		for (Entry<String, Object> e: tagToValue.entrySet()){
			String tag = e.getKey();
			XMLTransducer trans = TransducerFactory.getTransducerForTag(tag);
			JFLAPDebug.print(tag);
			if (trans == null)
				trans = new SymbolStringTransducer(e.getKey());
			root.appendChild(trans.toXMLTree(doc, e.getValue()));
		}
		return root;
	}

	public abstract Map<String, Object> createTagToValueMap(T structure);

}
