package file.xml.formaldef.components;

import model.formaldef.components.symbols.SpecialSymbol;

public abstract class SpecialSymbolTransducer<T extends SpecialSymbol>
													extends SingleValueTransducer<T> {

	@Override
	public Object retrieveData(T structure) {
		return structure.toSymbolObject();
	}

	
}
