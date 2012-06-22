package model.symbols.symbolizer;

import model.grammar.Grammar;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.symbols.Symbol;

public class GrammarDefaultSymbolizer extends DefaultSymbolizer {

	
	public GrammarDefaultSymbolizer(Grammar fd) {
		super(fd);
	}

	@Override
	public Symbol toNewSymbol(String s) {
		if (Character.isUpperCase(s.charAt(0))){
			return new Variable(s);
		}
		return new Terminal(s);
	}
	
}
