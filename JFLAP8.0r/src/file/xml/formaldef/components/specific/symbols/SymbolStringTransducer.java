package file.xml.formaldef.components.specific.symbols;

import java.util.Arrays;

import model.formaldef.components.alphabets.Alphabet;
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

	@Override
	public Object extractData(SymbolString structure) {
		return structure.toNondelimitedString();
	}

	@Override
	public SymbolString createInstance(String text) {
		return SymbolString.createFromString(text, myAlphs);
	}
	

}