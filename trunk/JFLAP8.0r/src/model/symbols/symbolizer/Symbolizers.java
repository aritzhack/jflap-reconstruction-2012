package model.symbols.symbolizer;

import model.formaldef.FormalDefinition;
import model.symbols.SymbolString;


public class Symbolizers {

	public static SymbolString symbolize(String in, FormalDefinition fd) {
		Symbolizer s = new AdvancedSymbolizer(fd);
		return s.symbolize(in);
	}

}
