package file.xml.formaldef.components.single;

import model.formaldef.components.symbols.Symbol;

public class SymbolTransducer extends SingleNodeTransducer<Symbol> {

	@Override
	public String getTag() {
		return "symbol";
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
