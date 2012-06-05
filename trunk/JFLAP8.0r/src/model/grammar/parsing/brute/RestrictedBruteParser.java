package model.grammar.parsing.brute;

import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.parsing.Derivation;

public class RestrictedBruteParser extends BruteParser {

	public RestrictedBruteParser(Grammar g) {
		super(g);
		// TODO Auto-generated constructor stub
	}





	// restricted only
	public int getNumberOfTerminals (SymbolString string) {
		int terminals = 0;

		for (int i = 0; i < string.size(); i++ ) {
			if (Grammar.isTerminal(string.get(i))) {
				terminals ++;
			}
		}
		return terminals;
	}





	@Override
	public boolean isPossibleDerivation(SymbolString string) {
		// TODO Auto-generated method stub
		return getNumberOfTerminals(string) <= getInput().size();
	}





	@Override
	public Derivation getDerivation() {
		// TODO Auto-generated method stub
		return null;
	}
}
