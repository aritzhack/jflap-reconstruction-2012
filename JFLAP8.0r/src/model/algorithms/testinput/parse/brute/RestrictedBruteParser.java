package model.algorithms.testinput.parse.brute;

import java.util.ArrayList;
import java.util.List;

import model.algorithms.testinput.parse.Derivation;
import model.grammar.Grammar;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class RestrictedBruteParser extends UnrestrictedBruteParser {

	public RestrictedBruteParser(Grammar g) {
		super(g);
	}

	public int getNumberOfTerminals (SymbolString string) {
		int terminals = 0;

		for (int i = 0; i < string.size(); i++ ) {
			if (Grammar.isTerminal(string.get(i))) {
				terminals ++;
			}
		}
		return terminals;
	}


	public boolean isPossibleDerivation(List<Derivation> derivationList, SymbolString derivation) {
		if (!super.isPossibleDerivation(derivation))
			return false;
		
		boolean startBookend = false, endBookend = false;
		ArrayList<SymbolString> discrete = new ArrayList<SymbolString>();

		/*
		 * Set the start and end "bookeneds", that is, the derivation is padded
		 * with terminals on either it's left or right sides.
		 */
		if (derivation.isEmpty()) {
			startBookend = endBookend = false;
		} else {
			startBookend = !Grammar.isVariable(derivation.getFirst());
			endBookend = !Grammar.isVariable(derivation.getLast());
		}

		/* Break up groups of terminals into the "discrete" array. */
		if (startBookend) discrete.add(new SymbolString());
		
		for (Symbol s: derivation) {
			if(Grammar.isVariable(s))
				discrete.add(new SymbolString());
			if(Grammar.isTerminal(s))
				discrete.get(discrete.size()-1).add(s);
		}
		if (!endBookend) discrete.remove(discrete.size()-1);
		
		
		int cp = 0;
		for (int i = 0; i < discrete.size(); i++) {
			SymbolString e = discrete.get(i);
			if (startBookend && i == 0) {
				if (!getInput().startsWith(e)){
					return false;
				}
				cp = e.size();
			} else if (endBookend && i == discrete.size() - 1) {
				if (!getInput().endsWith(e)){
					return false;
				}
			} else {
				if(cp>=discrete.size()){
					continue;
				}
				cp = getInput().indexOf(e, cp);
				if (cp == -1){
					return false;
				}
				cp += e.size();
			}
		}
		return true;
	}
}
