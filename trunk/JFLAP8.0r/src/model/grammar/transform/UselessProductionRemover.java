package model.grammar.transform;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;

import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;
import model.formaldef.components.alphabets.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.VariableAlphabet;

public class UselessProductionRemover extends GrammarTransformAlgorithm {

	private ProductionSet myDeriveTerms;
	private ProductionSet myFullDerivesTerminals;
	private ProductionSet myProcessedProductions;
	private Set<Variable> myVarsDeriveTerms;
	
	public UselessProductionRemover(Grammar g) {
		super(g);
		constructTerminalDerivationSet();
		//check if is last start production and none derived 
		if (noStartProductionsDeriveTerms())
			throw new AlgorithmException("No start productions derive terminals." +
												" Therefore this grammar cannot derive any strings " +
												"and cannot be transformed further.");
	}

	@Override
	public String getDescription() {
		return "Useless Production Remover";
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{new checkDerivesTerminals()};
	}

	@Override
	public String getDescriptionName() {
		return "Removes all useless productions in a grammar. A" +
				"look musi d";
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myDeriveTerms = new ProductionSet();
		myProcessedProductions = new ProductionSet();
		myVarsDeriveTerms = new TreeSet<Variable>();
		super.reset();
		this.getTransformedGrammar().setStartVariable(this.getOriginalGrammar().getStartVariable());
		return true;
	}

	
	////////Step 1: Get productions which derive terminals/////
	
	private void constructTerminalDerivationSet() {
		myFullDerivesTerminals = new ProductionSet();
		for (Production p : this.getOriginalGrammar().getProductionSet()){
			if(this.checkDerivesTerminals(p)){
				System.out.println(p);
				myFullDerivesTerminals.add(p);
			}
		}
	}

	private boolean checkDerivesTerminals(Production p) {
		return checkDerivesTerminals(p, new LinkedList<Production>());
	}

	private boolean checkDerivesTerminals(Production p,
			LinkedList<Production> history) {

		//if this production has already been seen, i.e. we are in a loop
		// then it cannot derive a terminal on this path
		if (history.contains(p)) return false;
		
		history.add(p);
		
		//loop through all variables on the RHS.
		// if each variable derives terminals,
		// then so does p!
		for (Variable v: p.getVariablesOnRHS()){
			
			if (!checkDerivesTerminals(v, history)){
				return false;
			}
		}
		myVarsDeriveTerms.add((Variable) p.getLHS().getFirst());
		return true;
	}

	private boolean checkDerivesTerminals(Variable v,
			LinkedList<Production> history) {
		//memoizing!
		if (myVarsDeriveTerms.contains(v)){
			return true;
		}
		
		SymbolString lhs = new SymbolString(v);
		ProductionSet productions = this.getOriginalGrammar().getProductionSet();
		for (Production prod: productions.getProductionsWithLHS(lhs)){
			LinkedList<Production> temp = new LinkedList<Production>(history);
			if (checkDerivesTerminals(prod, temp)){
				return true;
			}
		}

		return false;
	}

	private boolean noStartProductionsDeriveTerms() {
		StartVariable var = this.getOriginalGrammar().getStartVariable();
		return !myVarsDeriveTerms.contains(var);
	}

	private Production[] getUncheckedProductions() {
		Set<Production> all = new TreeSet<Production>(this.getOriginalGrammar().getProductionSet());
		all.removeAll(myProcessedProductions);
		return all.toArray(new Production[0]);
	}

	public boolean checkRemainingProductions(){
		for (Production p: this.getUncheckedProductions()){
			checkAndAddDerivesTerminals(p);
		}
		return true;
	}

	public boolean allProductionsChecked(){
		ProductionSet p = this.getOriginalGrammar().getProductionSet();
		return myProcessedProductions.size() == p.size();
	}

	public BooleanWrapper checkAndAddDerivesTerminals(Production p){
		
		//check in Production set
		ProductionSet prod = this.getOriginalGrammar().getProductionSet();
		if (!prod.contains(p))
			return new BooleanWrapper(false, "The production " + p + " is not a part of this grammar.");
		
		//check already checked
		if (this.hasProcessed(p))
			return new BooleanWrapper(false, "The production " + p + " has already been checked.");
		
		
		myProcessedProductions.add(p);
		
		//check derives terminals
		if (!myFullDerivesTerminals.contains(p))
			return new BooleanWrapper(false, "The production " + p + " does not derive terminals.");

		myDeriveTerms.add(p);
		this.getTransformedGrammar().getProductionSet().add(p);
		
		return new BooleanWrapper(true);
	}
	
	public boolean hasProcessed(Production p) {
		return myProcessedProductions.contains(p);
	}
	
	
	
	//////////////////////////////////////////////
	////////////// Algorithm Steps ///////////////
	//////////////////////////////////////////////
	
	private class checkDerivesTerminals implements AlgorithmStep {

		@Override
		public String getDescriptionName() {
			return "Check if Variable derives Terminal string.";
		}

		@Override
		public String getDescription() {
			return "Checks if, starting wit the variable in question," +
					"one can apply a sequence of productions such that" +
					"the result is all terminals";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return checkRemainingProductions();
		}

		@Override
		public boolean isComplete() {
			return allProductionsChecked();
		}
		
	}

}
