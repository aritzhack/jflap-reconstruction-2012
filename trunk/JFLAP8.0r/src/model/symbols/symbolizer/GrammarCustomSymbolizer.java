package model.symbols.symbolizer;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.Grammar;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.symbols.Symbol;

public class GrammarCustomSymbolizer extends CustomSymbolizer {

	

	private String myOpenGroup;
	private String myCloseGroup;

	public GrammarCustomSymbolizer(Grammar fd) {
		super(fd);
		myOpenGroup = fd.getOpenGroup()+"";
		myCloseGroup = fd.getCloseGroup()+"";
	}

	@Override
	public Symbol toNewSymbol(String s) {
		if (s.startsWith(myOpenGroup) && s.endsWith(myCloseGroup)){
			return new Variable(s);
		}
		return new Terminal(s);
	}
	
}
