package file.xml.formaldef.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.SymbolString;
import file.xml.Transducer;
import file.xml.XMLHelper;
import file.xml.formaldef.components.single.SymbolStringTransducer;

public abstract class FunctionTransducer<T extends LanguageFunction> implements Transducer<T> {

	
	private Alphabet[] myAlphs;
	
	public FunctionTransducer(Alphabet ... alphs){
		myAlphs = alphs;
	}
	
	@Override
	public T fromStructureRoot(Element root) {
		List<Element> eleChildren = XMLHelper.getElementChildren(root);
		Map<String, SymbolString> valueMap = new HashMap<String, SymbolString>();
		for (Element e: eleChildren){
			String tag = e.getTagName();
			Alphabet[] alphs = 
					FunctionAlphabetFactory.discerneAlphabets(tag, myAlphs);
			SymbolStringTransducer trans = new SymbolStringTransducer(tag, alphs);
			valueMap.put(tag, trans.fromStructureRoot(e));			
		}
		return createFunction(valueMap);
	}

	public abstract T createFunction(Map<String, SymbolString> valueMap);
	
	@Override
	public Element toXMLTree(Document doc, T structure) {
		Map<String, SymbolString> tagToValue = createTagToValueMap(structure);
		Element root = XMLHelper.createElement(doc, getTag(), null, null);
		for (Entry<String, SymbolString> e: tagToValue.entrySet()){
			SymbolStringTransducer transducer = new SymbolStringTransducer(e.getKey());
			root.appendChild(transducer.toXMLTree(doc, e.getValue()));
		}
		return root;
	}

	public abstract Map<String, SymbolString> createTagToValueMap(T structure);

}
