package file.xml.formaldef.components.symbols;

import java.util.Arrays;

import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.DefaultSymbolizer;
import model.symbols.symbolizer.Symbolizer;

import org.w3c.dom.Element;

import debug.JFLAPDebug;
import file.xml.formaldef.components.SingleNodeTransducer;

public class SymbolStringTransducer extends SingleNodeTransducer<SymbolString> {

	private Symbolizer mySymbolizer;

	public SymbolStringTransducer(String tag, Alphabet ... alphs){
		this(tag, new DefaultSymbolizer(alphs));
	}


	public SymbolStringTransducer(String tag, Symbolizer symbolizer) {
		super(tag);
		mySymbolizer = symbolizer;

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
		return mySymbolizer.symbolize(text);
	}
	

}
