package model.grammar.parsing.brute;

import java.util.HashSet;
import java.util.Set;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;

public class UnrestrictedBruteParser extends BruteParser{

	public UnrestrictedBruteParser(Grammar g){
		super(optimize(g));
	}
	
	/**
	 * Given an unrestricted grammar, this will return an unrestricted grammar
	 * with fewer productions that accepts the same language.
	 * 
	 * @param grammar
	 *            the input grammar
	 * @return a grammar with productions some subset of the original grammar,
	 *         or <CODE>null</CODE> in the special case where no production
	 *         with just the start variable on the LHS exists (i.e. the grammar
	 *         accepts no language, though if a grammar accepts no language this
	 *         method is NOT gauranteed to return <CODE>null</CODE>)
	 */
	private static Grammar optimize(Grammar grammar) {
		Variable startVariable = grammar.getStartVariable();
		Production[] prods = grammar.getProductionSet().toArray();
		// Which symbols in the grammar may possibly lead to just
		// terminals? First, we just add all those symbols with just
		// terminals on the right hand side.
		Set<Symbol> terminating = new HashSet<Symbol>();
		// Add those variables that lead to success.
		boolean[] added = new boolean[prods.length];
		for (int i = 0; i < prods.length; i++) {
			added[i] = false;
			if (prods[i].getVariablesOnRHS().size() == 0) {
				terminating.addAll(prods[i].getUniqueSymbolsUsed());
				added[i] = true;
			}
		}
		// Repeat
		boolean changed;
		do {
			changed = false;
			// If a production has only "terminating" variables, add it.
			for (int i = 0; i < prods.length; i++) {
				Set<Variable> l = prods[i].getVariablesOnRHS();
				if (!added[i] && terminating.containsAll(l)) {
					terminating.addAll(prods[i].getUniqueSymbolsUsed());
					added[i] = changed = true;
				}
			}
		} while (changed);
		Grammar g = grammar.alphabetAloneCopy();
		g.setStartVariable(grammar.getStartVariable());
		// Need to find a production with just the start var on LHS.
		int i;
		for (i = 0; i < prods.length; i++){
			if (added[i] && prods[i].getLHS().size()==1 && prods[i].getLHS().getFirst().equals(startVariable)){
				break;
			}
		}
		if (i == prods.length)
			return null;
		
		added[i] = true;
		for (i = 0; i < prods.length; i++)
			if (added[i])
				g.getProductionSet().add(prods[i]);
		return g;
	}
}
