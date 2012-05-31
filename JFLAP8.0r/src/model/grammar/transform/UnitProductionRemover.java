package model.grammar.transform;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmExecutingStep;
import model.algorithms.AlgorithmStep;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.typetest.matchers.ContextFreeChecker;

public class UnitProductionRemover extends ProductionRemovalAlgorithm {

	private ProductionSet myNonUnitProductions;
	private ConstructDependencyGraphStep myDependencyGraphStep;

	public UnitProductionRemover(Grammar g) {
		super(g);
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTargetProductionType() {
		return "Unit";
	}
	
	@Override
	public AlgorithmStep[] initializeAllSteps() {
		AlgorithmStep[] steps = super.initializeAllSteps();
		myDependencyGraphStep = new ConstructDependencyGraphStep();
		return new AlgorithmStep[]{steps[0],
					myDependencyGraphStep,
					steps[1]};
	}
	
	@Override
	public boolean reset() throws AlgorithmException {
		myNonUnitProductions = getNonUnitProductions();
		return super.reset();
	}
	
	
	private ProductionSet getNonUnitProductions() {
		ProductionSet nonUnit = new ProductionSet();

		for (Production p: this.getOriginalGrammar().getProductionSet()){
			if (!isOfTargetForm(p))
				nonUnit.add(p);
		}
		return nonUnit;
	}

	@Override
	public boolean isOfTargetForm(Production p) {
		SymbolString rhs = p.getRHS();
		return rhs.size() == 1 && Grammar.isVariable(rhs.get(0));
	}

	@Override
	public Set<Production> getProductionsToAddForRemoval(Production p) {
		Set<Production> toAdd = new TreeSet<Production>();
		Variable lhsVar = (Variable) p.getLHS().getFirst();
		Variable rhsVar = (Variable) p.getLHS().getFirst();
		
		if (lhsVar.equals(rhsVar))
			return toAdd;
		
		DependencyGraph graph = myDependencyGraphStep.getAlgorithm().getDependencyGraph();
		
		Variable[] dep = graph.getAllDependencies(lhsVar);
		
		for(Variable v: dep){
			toAdd.addAll(myNonUnitProductions.getProductionsWithSymbolOnLHS(v));
		}
		
		return toAdd;
	}
	
	private class ConstructDependencyGraphStep extends AlgorithmExecutingStep<ConstructDependencyGraph>{

		@Override
		public ConstructDependencyGraph initializeAlgorithm() {
			return new ConstructDependencyGraph(getOriginalGrammar()) {
				@Override
				public Map<Variable, Set<Variable>> getDependenciesFromProd(
						Production p) {
					ContextFreeChecker checker = new ContextFreeChecker();
					//if the production is context free, and has only a variable
					//on the rhs then it is ok!
					if (checker.matchesProduction(p) && 
							p.getRHS().size() == 1 &&
							Grammar.isVariable(p.getRHS().getFirst()))
						return super.getDependenciesFromProd(p);
					return new TreeMap<Variable, Set<Variable>>();
				}
			};
		}
		
	}

}
