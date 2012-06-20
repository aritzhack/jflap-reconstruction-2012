package model.grammar.parsing.brute;

import java.util.ArrayList;

import debug.JFLAPDebug;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.parsing.Derivation;

public class RestrictedBruteParser extends BruteParser {

	public RestrictedBruteParser(Grammar g) {
		super(g);
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


	public boolean isPossibleDerivation(SymbolString derivation) {
		if (!super.isPossibleDerivation(derivation))
			return false;
		//int targetSearched = 0;
		boolean startBookend = false, endBookend = false;
		ArrayList<SymbolString> discrete = new ArrayList<SymbolString>();
		//int start = -1;

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





	@Override
	public Derivation getDerivation() {
		// TODO Auto-generated method stub
		return null;
	}
}
