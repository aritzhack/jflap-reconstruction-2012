package file.xml;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import jflap.errors.JFLAPError;
import jflap.model.formaldef.AlphabetException;
import jflap.model.formaldef.FormalDefinition;
import jflap.model.formaldef.alphabets.*;
import jflap.model.formaldef.alphabets.specific.*;
import jflap.model.formaldef.grouping.GroupingPair;
import jflap.model.formaldef.grouping.IGrouping;
import jflap.model.formaldef.symbols.Symbol;
import jflap.preferences.JFLAPpreferences;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;



public class AlphabetXMLFactory {

	private static final String ALPHABET_TAG = "alphabet",
								SYMBOL_TAG = "symbol",
								CHAR_SEQUENCE_TAG = "chars",
								SPECIAL_TAG = "special",
								TRUE = "1",
								FALSE = "0",
								ALPHABET_TYPE_TAG = "type",
								OPEN_GROUPING_TAG = "open",
								CLOSE_GROUPING_TAG = "close";
	
	private static HashMap<String, Class<? extends Alphabet>> TagToAlphabetMap;

	static{
		TagToAlphabetMap = new HashMap<String, Class<? extends Alphabet>>();
		TagToAlphabetMap.put("variable", VariableAlphabet.class);
		TagToAlphabetMap.put("terminal", TerminalAlphabet.class);
		TagToAlphabetMap.put("input", InputAlphabet.class);
		TagToAlphabetMap.put("output", OutputAlphabet.class);
		TagToAlphabetMap.put("tape", TapeAlphabet.class);
		TagToAlphabetMap.put("stack", StackAlphabet.class);
		
		
	}
	
	
	public static Iterable<Alphabet> getAlphabets(Element root, Class<? extends FormalDefinition> clazz) {
		
		// Check for the type tag.
		NodeList typeNodes = root.getElementsByTagName(ALPHABET_TAG);
		
		if (typeNodes.getLength() == 0)
			return JFLAPpreferences.getDefaultDefintions().getDefinitionByClass(clazz).getAlphabets();
			
		
		ArrayList<Alphabet> alphs = new ArrayList<Alphabet>();
		
		for (int i = 0; i < typeNodes.getLength(); i++){
			alphs.add(createAlphabet(typeNodes.item(i)));
		}
		
		return alphs;
	}

	private static Alphabet createAlphabet(Node parent) {
		
		
		String alphType = ((Element) parent).getAttribute(ALPHABET_TYPE_TAG);
		Alphabet a = null;
		try {
			a = TagToAlphabetMap.get(alphType).newInstance();
		} catch (Exception e) {
			new AlphabetException("Error importing file. No alphabet of the tag " + alphType + " exists.");
		} 
		
		//If this could use grouping, check for grouping!
		if (a instanceof IGrouping){
			String open = ((Element) parent).getAttribute(OPEN_GROUPING_TAG);
			String close = ((Element) parent).getAttribute(CLOSE_GROUPING_TAG);
			((IGrouping) a).setGrouping(new GroupingPair(open, close));
				
		}
			
		NodeList typeNodes = parent.getChildNodes();
		for (int i = 0; i < typeNodes.getLength(); i++){
			if (typeNodes.item(i).getNodeType() == Node.ELEMENT_NODE)
				a.add(createSymbol((Element)typeNodes.item(i)));
		}

		
		return a;
	}

	private static Symbol createSymbol(Element item) {
		Symbol s = null;
		try {
			s = new Symbol(item.getAttribute(CHAR_SEQUENCE_TAG));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if (item.getAttribute(SPECIAL_TAG).equals(TRUE)){
			s.setSpecial(true);
		}
		return s;
	}

	public static Node createAlphabetElement(Document doc, Alphabet a) {
		Element alphParent = doc.createElement(ALPHABET_TAG);
		alphParent.setAttribute(ALPHABET_TYPE_TAG, getAlphabetTag(a.getClass()));
		String open = "",
				close = "";
		if (a instanceof IGrouping){
			open = ((IGrouping) a).getGrouping().getOpenGroup();
			close = ((IGrouping) a).getGrouping().getCloseGroup();
		}
		
		alphParent.setAttribute(OPEN_GROUPING_TAG, open);
		alphParent.setAttribute(CLOSE_GROUPING_TAG, close);
		
		for (Symbol s: a){
			alphParent.appendChild(createSymbolElement(s, doc));
		}
		return alphParent;
	}

	private static Node createSymbolElement(Symbol s, Document doc) {
		Element symbol = doc.createElement(SYMBOL_TAG);
		
		symbol.setAttribute(CHAR_SEQUENCE_TAG, s.getString());
		symbol.setAttribute(SPECIAL_TAG, s.isSpecial() ? TRUE : FALSE);
		
		return symbol;
	}

	private static String getAlphabetTag(
			Class<? extends Alphabet> clz) {
		
		for(Entry<String, Class<? extends Alphabet>> e: TagToAlphabetMap.entrySet()){
			if (e.getValue().isAssignableFrom(clz))
				return e.getKey();
		}
		
		throw new AlphabetException("No string tag ahs been defined for the Alphabet type " + clz.toString());
	}

}
