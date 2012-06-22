package model.symbols.symbolizer;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class CustomSymbolizer extends AbstractSymbolizer {

	
	
	public CustomSymbolizer(Alphabet... alphs) {
		super(alphs);
	}
	

	public CustomSymbolizer(FormalDefinition fd) {
		super(fd);
	}


	@Override
	public String[] splitToSymbols(String unIDed) {
		return new String[]{unIDed};
	}

}
