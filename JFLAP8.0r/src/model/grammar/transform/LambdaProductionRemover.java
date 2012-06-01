package model.grammar.transform;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;

public class LambdaProductionRemover extends ProductionRemovalAlgorithm {

	private Set<Production> myMemory;

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
	public boolean reset() throws AlgorithmException {
		 myMemory = new TreeSet<Production>();
		return super.reset();
	}
	 
	@Override
	public String getTargetProductionType() {
		return "Lambda";
	}

	@Override
	public boolean isOfTargetForm(Production p) {
		return recursiveDerivesLambda(p, new TreeSet<Production>());
	}

	public boolean isLambdaProduction(Production p) {
		return p.getRHS().isEmpty();
	}

	private boolean recursiveDerivesLambda(Variable v, Set<Production> history) {
		ProductionSet prodSet = this.getOriginalGrammar().getProductionSet();
		//if one prod with v on lhs derives lambda then v derives lambda
		for (Production p: prodSet.getProductionsWithSymbolOnLHS(v)){
			if (recursiveDerivesLambda(p, history)){
				myMemory.add(p); //memoize
				return true;
			}
		}
		return false;
	}

	private boolean recursiveDerivesLambda(Production p, Set<Production> history) {
		//check memory
		if (myMemory.contains(p))
			return true;
		//check if it is a lambda prod
		if (isLambdaProduction(p))
			return true;
		//check if we have already looked at this prod
		if (history.contains(p))
			return false;
		//check if it has any terminals on RHS
		if (!p.getTerminalsOnRHS().isEmpty())
			return false;
		
		history = new TreeSet<Production>(history);
		history.add(p);
		//check if each variable on rhs derives lambda
		for (Variable v: p.getVariablesOnRHS()){
			if (!recursiveDerivesLambda(v, history))
				return false;
		}
		return true;
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
