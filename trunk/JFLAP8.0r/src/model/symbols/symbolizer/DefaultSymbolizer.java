package model.symbols.symbolizer;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class DefaultSymbolizer extends AbstractSymbolizer {

	public DefaultSymbolizer(FormalDefinition fd) {
		super(fd);
	}
	
	public DefaultSymbolizer(Alphabet ... alphs) {
		super(alphs);
	}

	@Override
	public String[] splitToSymbols(String unIDed) {
		char[] chars = unIDed.toCharArray();
		String[] split = new String[chars.length];
		for (int i = 0; i < chars.length; i++)
			split[i]= chars[i]+"";
		return split;
	}
	
	

}
