package file.xml.formaldef.components.symbols;

import file.xml.formaldef.components.SingleNodeTransducer;
import model.formaldef.components.symbols.Symbol;

public class SymbolTransducer extends SingleNodeTransducer<Symbol> {

	public SymbolTransducer(String tag) {
		super(tag);
	}

	@Override
	public Object extractData(Symbol structure) {
		return structure.getString();
	}

	@Override
	public Symbol createInstance(String text) {
		return new Symbol(text);
	}

}
