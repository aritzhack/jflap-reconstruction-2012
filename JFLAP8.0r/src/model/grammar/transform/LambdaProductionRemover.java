package model.grammar.transform;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import errors.BooleanWrapper;

import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;

/**
 * Algorithm for removing lambda productions from a CFG. Broken
 * down into three primary steps:
 * 
 * 		1. Identify all lambda transitions, i.e. transitions of
 * 			the form: A->lambda
 * 		2. Identify all other productions which derive lambda
 * 				A->X1 X2 ... XN,   XI is a variable, XI derives lambda
 * 		3. 
 * 
 * @author Julian
 *
 */
public class LambdaProductionRemover extends ProductionIdentifyAlgorithm {

	private Set<Variable> myLambdaVariables;
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
		if (!super.reset())
			return false;
		myLambdaVariables = new TreeSet<Variable>();
		return true;
	}
	 
	@Override
	public boolean isOfTargetForm(Production p) {
		return recursiveDerivesLambda(p, new TreeSet<Production>());
	}

	@Override
	public Set<Production> getProductionsToAddForRemoval(Production p) {
		Set<Production> toAdd = new TreeSet<Production>();
		Symbol start = this.getOriginalGrammar().getStartVariable();
		Symbol lhs = p.getLHS().getFirst();
		if (lhs.equals(start))
			return toAdd;
		
		ProductionSet prods = this.getTransformedGrammar().getProductionSet();
		Set<Production> varOnRHS = prods.getProductionsWithSymbolOnRHS(lhs);
			
		for (Production pRHS : varOnRHS) {
				toAdd.addAll(doAllPossibleSubs(pRHS,lhs));
		}
		
		return toAdd;
	}

	public boolean isLambdaProduction(Production p) {
		return p.getRHS().isEmpty();
	}

	private boolean recursiveDerivesLambda(Variable v, Set<Production> history) {
		ProductionSet prodSet = this.getOriginalGrammar().getProductionSet();
		//if one prod with v on lhs derives lambda then v derives lambda
		for (Production p: prodSet.getProductionsWithSymbolOnLHS(v)){
			if (recursiveDerivesLambda(p, history)){
				myMemory.add(p);
				return true;
			}
		}
		return false;
	}

	private boolean recursiveDerivesLambda(Production p, Set<Production> history) {
		//check already determined lambda productions.
		if (myMemory.contains(p))
			return true;
		//check if lambda production
		if(isLambdaProduction(p))
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
	
	private int[] getIndeciesOfTarget(SymbolString rhs, Symbol target) {
		int[] index = new int[rhs.size()];
		int j = 0;
		for (int i = 0; i < rhs.size(); i++){
			if (rhs.get(i).equals(target)){
				index[j++] = i;
			}
		}
		
		return Arrays.copyOfRange(index, 0, j);
	}

	@Override
	protected boolean shouldRemove(Production p) {
		return isLambdaProduction(p);
	}
	
	@Override
	public String getIdentifyStepName() {
		return "Identify all Lambda Productions";
	}
	
	@Override
	public BooleanWrapper identifyProductionToBeRemoved(Production p) {
		BooleanWrapper bw = super.identifyProductionToBeRemoved(p) ;
		if (!bw.isError())
			myLambdaVariables.add((Variable) p.getLHS().getFirst());
		return bw;
	}

}
