package file.xml.formaldef.components.alphabet;

import model.formaldef.components.SetComponent;
import model.formaldef.components.symbols.Symbol;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.Copyable;

import file.xml.XMLHelper;
import file.xml.formaldef.components.SetComponentTransducer;
import file.xml.formaldef.components.symbols.SymbolTransducer;


public abstract class AlphabetTransducer extends SetComponentTransducer<Symbol> {

	private SymbolTransducer mySymbolTransducer;
	public static final String ALPH_TAG_BASE = "alph";

	public AlphabetTransducer(){
		mySymbolTransducer = new SymbolTransducer();
	}

	@Override
	public Symbol decodeSubNode(Element item) {
		return mySymbolTransducer.fromStructureRoot(item);
	}

	@Override
	public String getSubNodeTag() {
		return mySymbolTransducer.getTag();
	}

	@Override
	public Element createSubNode(Document doc, Symbol item) {
		return mySymbolTransducer.toXMLTree(doc, item);
	}
	
}
