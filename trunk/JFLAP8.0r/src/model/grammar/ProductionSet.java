package model.grammar;

import model.formaldef.components.alphabets.symbols.SymbolString;
import model.formaldef.components.functionset.FunctionSet;

public class ProductionSet extends FunctionSet<Production> {

	@Override
	public Character getCharacterAbbr() {
		return 'P';
	}

	@Override
	public String getDescriptionName() {
		return "Production Rules";
	}

	@Override
	public String getDescription() {
		return "The set of production rules for a grammar.";
	}

	/**
	 * Retrieve all productions in this {@link ProductionSet}
	 * of the form:
	 * 		lhs -> A*
	 * where A* is any string of variables and terminals.
	 * @param lhs
	 * @return
	 */
	public Production[] getProductionsWithLHS(SymbolString lhs) {
		ProductionSet prods = new ProductionSet();
		for (Production p: this){
			if (p.getLHS().equals(lhs))
				prods.add(p);
		}
		return prods.toArray(new Production[0]);
	}

}
