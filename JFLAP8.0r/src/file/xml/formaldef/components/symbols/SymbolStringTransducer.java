package file.xml.formaldef.components.symbols;

import java.util.Arrays;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

import org.w3c.dom.Element;

import debug.JFLAPDebug;
import file.xml.formaldef.components.SingleNodeTransducer;

public class SymbolStringTransducer extends SingleNodeTransducer<SymbolString> {

	private String myTag;
	private Alphabet[] myAlphs;

	public SymbolStringTransducer(String tag, Alphabet ... alphs){
		myTag = tag;
		myAlphs = alphs;
	}

	@Override
	public String getTag() {
		return myTag;
	}

	public Object extractData(Symbol[] structure){
		return this.extractData(new SymbolString(structure));
	}
	
	@Override
	public Object extractData(SymbolString structure) {
		return structure.toNondelimitedString();
	}

	@Override
	public SymbolString createInstance(String text) {
		return SymbolString.createFromDefinition(text, myAlphs);
	}
	

}
