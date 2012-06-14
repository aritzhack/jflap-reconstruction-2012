package model.grammar;

import java.util.Set;
import java.util.TreeSet;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;

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

	@Override
	public ProductionSet copy() {
		return (ProductionSet) super.copy();
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

	public Set<Production> getProductionsWithSymbolOnLHS(Symbol s) {
		Set<Production> prods = new TreeSet<Production>();
		for (Production p: this){
			if (p.getLHS().contains(s))
				prods.add(p);
		}
		
		return prods;
	}
	
	public Set<Production> getProductionsWithSymbolOnRHS(Symbol s) {
		Set<Production> prods = new TreeSet<Production>();
		for (Production p: this){
			if (p.getRHS().contains(s))
				prods.add(p);
		}
		
		return prods;
	}
	
	@Override
	public Production[] toArray() {
		return super.toArray(new Production[0]);
	}

	public Set<Variable> getVariablesUsed() {
		return getSymbolsOfType(Variable.class);
	}

	public Set<Terminal> getTerminalsUsed() {
		return getSymbolsOfType(Terminal.class);
	}

	private <T extends Symbol> Set<T> getSymbolsOfType(Class<T> clz) {
		Set<Symbol> used = this.getUniqueSymbolsUsed();
		Set<T> toReturn = new TreeSet<T>();
		for (Symbol s: used){
			if (clz.isAssignableFrom(s.getClass()))
				toReturn.add(clz.cast(s));
		}
		
		return toReturn;
	}

}
