package model.algorithms.conversion.autotogram;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;
import model.formaldef.components.alphabets.symbols.Variable;
import model.grammar.Production;

public class FSAtoRegGrammarConversion extends AutomatonToGrammarConversion<FiniteStateAcceptor, FSAVariableMapping, FiniteStateTransition> {

	private Set<State> finalStatesHandled;

	public FSAtoRegGrammarConversion(FiniteStateAcceptor automaton)
			throws AlgorithmException {
		super(automaton);
	}

	@Override
	public String getDescriptionName() {
		return "FSA to Regular Grammar Conversion";
	}

	@Override
	public String getDescription() {
		return "Converts a finite state automaton to a right-linear grammar.";
	}

	@Override
	public boolean doFinalSteps() {
		boolean added = true;
		for (State s: this.getAutomaton().getFinalStateSet()){
			added &= addFinalStateProduction(s);
		}
		return added;
	}

	private boolean addFinalStateProduction(State s) {
		Variable v = this.getVarForMapping(new FSAVariableMapping(s));
		SymbolString lhs = new SymbolString(v);
		Production p = new Production(lhs, new SymbolString());

		boolean added = this.getConvertedGrammar().getProductionSet().add(p);
		if (added) added &= finalStatesHandled.add(s);
		return added;
	}

	@Override
	public boolean isStartMapping(FSAVariableMapping mapping) {
		State s = mapping.getState();
		return s.equals(this.getAutomaton().getStartState());
	}

	@Override
	public Production[] convertTransition(FiniteStateTransition trans) {
		FSAVariableMapping from = new FSAVariableMapping(trans.getFromState());
		FSAVariableMapping to = new FSAVariableMapping(trans.getToState());
		SymbolString lhs = new SymbolString(this.getVarForMapping(from));
		SymbolString rhs = new SymbolString(convertToTerminals(trans.getInput()));
		rhs.add(this.getVarForMapping(to));
		Production p = new Production(lhs, rhs);
		return new Production[]{p};
	}

	@Override
	public Set<FSAVariableMapping> getAllNecessaryMappings() {
		Set<FSAVariableMapping> mappings = new HashSet<FSAVariableMapping>();
		for (State s: this.getAutomaton().getStates()){
			mappings.add(new FSAVariableMapping(s));
		}
		return mappings;
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(FiniteStateAcceptor automaton) {
		return new BooleanWrapper[0];
	}

	@Override
	public boolean isComplete() {
		return super.isComplete() && allFinalStatesHandled();
	}

	private boolean allFinalStatesHandled() {
		Set<State> finalStates = new HashSet<State>(this.getAutomaton().getFinalStateSet());
		finalStates.removeAll(finalStatesHandled);
		return finalStates.isEmpty();
	}

	@Override
	public boolean reset() throws AlgorithmException {
		finalStatesHandled = new HashSet<State>();
		return super.reset();
	}


}
