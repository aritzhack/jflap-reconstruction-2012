package model.symbols.symbolizer;

import model.formaldef.FormalDefinition;
import model.regex.RegularExpression;

public class RegExDefaultSymbolizer extends DefaultSymbolizer {

	public RegExDefaultSymbolizer(RegularExpression fd) {
		super(fd.getOperators(), fd.getInputAlphabet());
	}

	
	
}
