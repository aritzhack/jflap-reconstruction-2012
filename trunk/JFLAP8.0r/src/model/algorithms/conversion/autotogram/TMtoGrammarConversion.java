package model.algorithms.conversion.autotogram;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;

import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineTransition;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Production;
import model.grammar.ProductionSet;
import errors.BooleanWrapper;

public class TMtoGrammarConversion extends AutomatonToGrammarConversion<TuringMachine, TMVariableMapping, TuringMachineTransition>{

	public TMtoGrammarConversion(TuringMachine automaton)
			throws AlgorithmException {
		super(automaton);
		
	}

	@Override
	public String getDescriptionName() {
		return "Turing machine to Grammar conversion";
	}

	@Override
	public String getDescription() {
		return "Algorithm (from Linz book) to convert TM to Unrestricted Grammar";
	}

	@Override
	public boolean isStartMapping(TMVariableMapping mapping) {
		return false;
	}

	@Override
	public Production[] convertTransition(TuringMachineTransition trans) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<TMVariableMapping> getAllNecessaryMappings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(TuringMachine fd) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public AlgorithmStep[] initializeAllSteps() {
		LinkedList<AlgorithmStep> steps = new LinkedList<AlgorithmStep>();
		steps.addAll(Arrays.asList(super.initializeAllSteps()));
		steps.add(2, new CreateAdditionalProductions());
		return steps.toArray(new AlgorithmStep[0]);
	}
	
	private class CreateAdditionalProductions implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return addAllExtraProductions();
		}

		@Override
		public boolean isComplete() {
			return !getConvertedGrammar().getProductionSet().isEmpty();
		}
		
	}
	private boolean addAllExtraProductions() {
		ProductionSet p = getConvertedGrammar().getProductionSet();
		String s = "S", t="T";
		while (getConvertedGrammar().getVariables().containsSymbolWithString(s)){
			s += "'";
		}
		while (getConvertedGrammar().getVariables().containsSymbolWithString(t)){
			t+= "'";
		}
		Variable S = new Variable(s), T = new Variable(t);
		getConvertedGrammar().setStartVariable(S);
		
		Variable Vblankblank = getVarForMapping(new TMVariableMapping(getOriginalDefinition().getBlankSymbol(),getOriginalDefinition().getBlankSymbol()));
		p.add(new Production(S, T));
		p.add(new Production(S, Vblankblank, S));
		p.add(new Production(S, S, Vblankblank));
		p.add(new Production(getOriginalDefinition().getBlankSymbol(), new SymbolString()));
		for(Symbol a : getAutomaton().getInputAlphabet()){
			p.add(new Production(T, T, getVarForMapping(new TMVariableMapping(a,a))));
			p.add(new Production(T, getVarForMapping(new TMVariableMapping(a, getOriginalDefinition().getStates().getStateWithID(0), a))));
		}
		return true;
	}
}
