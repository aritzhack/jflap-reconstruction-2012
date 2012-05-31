package model.grammar.transform;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import model.algorithms.AlgorithmStep;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;

public class LambdaProductionRemover extends ProductionRemovalAlgorithm {

	public LambdaProductionRemover(Grammar g) {
		super(g);
	}

	@Override
	public String getDescriptionName() {
		return "Lambda Production Remover";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTargetProductionType() {
		return "Lambda";
	}

	@Override
	public boolean isOfTargetForm(Production p) {
		return p.getRHS().isEmpty();
	}

	@Override
	public Set<Production> getProductionsToAddForRemoval(Production p) {
		Set<Production> toAdd = new TreeSet<Production>();
		Symbol start = this.getOriginalGrammar().getStartVariable();
		Symbol lhs = p.getLHS().getFirst();
		if (lhs.equals(start))
			return toAdd;
		
		ProductionSet prods = this.getOriginalGrammar().getProductionSet();
		Set<Production> varOnRHS = prods.getProductionsWithSymbolOnRHS(lhs);
			
		for (Production pRHS : varOnRHS) {
				toAdd.addAll(doAllPossibleSubs(pRHS,lhs));
		}
		
		return toAdd;
	}

	private Set<Production> doAllPossibleSubs(Production pRHS,
			Symbol target) {
		Set<Production> toAdd = new TreeSet<Production>();
		for (int i: getIndeciesOfTarget(pRHS.getRHS(),target)){
			SymbolString subInto = new SymbolString(pRHS.getRHS());
			subInto.remove(i);
			Production substituted = new Production(pRHS.getLHS(),subInto);
			toAdd.addAll(doAllPossibleSubs(substituted, target));
		}
		return toAdd;
	}


}
