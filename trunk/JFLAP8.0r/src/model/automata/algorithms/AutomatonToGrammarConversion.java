package model.automata.algorithms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.algorithms.AlgorithmException;
import model.algorithms.SteppableAlgorithm;
import model.automata.Automaton;
import model.automata.Transition;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.components.alphabets.grouping.SpecialSymbolFactory;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.Terminal;
import model.formaldef.components.alphabets.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import errors.BooleanWrapper;

public abstract class AutomatonToGrammarConversion<T extends Automaton<E>, S extends VariableMapping, E extends Transition> 
																			implements SteppableAlgorithm {
	/**
	 * The {@link Grammar} that is being created from the {@link Automaton};
	 */
	private Grammar myConvertedGrammar;
	
	/**
	 * The {@link Automaton} being converted to a {@link Grammar}
	 */
	private T myAutomaton;

	/**
	 * The list of all {@link VariableMapping} that need to be
	 * mapped to a variable before the algorithm can proceed
	 */
	private Set<S> allMappings;
	
	/**
	 * The {@link Map} of {@link VariableMapping} to {@link Variable}
	 * that have already been added.
	 */
	private Map<S, Variable> myMappedVariables;

	/**
	 * The set of {@link Transition} from the {@link Automaton} that
	 * have already been converted to productions.
	 */
	private HashSet<Transition> myConvertedTransitions;

	
	
	public AutomatonToGrammarConversion(T automaton) throws AlgorithmException{
		myAutomaton = automaton;
		allMappings = getAllNecessaryMappings(myAutomaton);
		BooleanWrapper bw = checkOfProperForm(automaton);
		if (bw.isFalse())
			throw new AlgorithmException(bw.getMessage());
		if (this.reset())
			throw new AlgorithmException("There an error occured with the initialization " +
											"of the converted Grammar.");
	}
	
	public Grammar getConvertedGrammar(){
		return myConvertedGrammar;
	}
	
	public T getAutomaton(){
		return myAutomaton;
	}

	@Override
	public boolean step() throws AlgorithmException{
		
		//convert input alphabet
		if (!inputAlphabetConverted()){
			return convertInputAlphabet();
		}
		//do automatic variable mappings
		else if(!variableMappingsComplete()){
			return doAllAutomaticVariableMappings();
		}
		//convert all transitions
		else if(!allTransitionsConverted()){
			return convertRemainingTransitions();
		}
		else if(!isComplete()){
			return doFinalSteps();
		}

		throw new AlgorithmException("The conversion is complete and can no longer " +
											"be stepped into.");
		
	}

	/**
	 * Checks if the {@link AutomatonToGrammarConversion} is complete. This method should be 
	 * overriden if more cases are necessary for completion.
	 * @return
	 */
	public boolean isComplete() {
		return inputAlphabetConverted() && 
				variableMappingsComplete() && 
				allTransitionsConverted() &&
				this.getConvertedGrammar().isComplete().length == 0;
	}

	public boolean convertRemainingTransitions() {
		boolean added = true;
		for (E trans: this.getUnconvertedTransitions()){
			added &= this.convertAndAddTransition(trans);
		}
		return added;
	}

	public boolean convertAndAddTransition(E trans) {
		Production[] p = this.convertTransition(trans);
		return this.getConvertedGrammar().getProductionSet().addAll(Arrays.asList(p));
	}

	public boolean doAllAutomaticVariableMappings(){
		for (S mapping: this.getUnmappedMappings()){
			Variable auto = new Variable(mapping.createDefaultVariableString());
			BooleanWrapper bw = this.addMapping(mapping, auto);
			if (bw.isFalse())
				throw new AlgorithmException(bw.getMessage());
		}
		return true;
	}
	
	public BooleanWrapper addMapping(S mapping, Variable var) {
		if(myMappedVariables.containsKey(mapping))
			return new BooleanWrapper(false, "The mapping " + mapping.toString() + " already exists" +
												" for in this conversion.");
		else if (this.getConvertedGrammar().getVariables().contains(var)){
			return new BooleanWrapper(false, "You have already added a mapping to the " +
										"variable " + var);
		}
		
		myMappedVariables.put(mapping, var);
		boolean added = myConvertedGrammar.getVariables().add(var);
		
		if (added && isStartMapping(mapping)){
			if(myConvertedGrammar.getStartVariable().isComplete().isTrue())
				throw new AlgorithmException("A Start Variable mapping has already been added " +
						"to the Converted grammar.");
			myConvertedGrammar.setStartVariable(var);
		}
		
		return new BooleanWrapper (added, 
							"Could not add the variable " + var + "to the converted Grammar.");
	}

	public boolean convertInputAlphabet() {
		boolean converted = true;
		for (Symbol s: myAutomaton.getInputAlphabet()){
			converted = converted && myConvertedGrammar.getTerminals().add(new Terminal(s.toString()));
		}
		return converted;
	}

	public boolean inputAlphabetConverted() {
		return myConvertedGrammar.getTerminals().size() == myAutomaton.getInputAlphabet().size();
	}

	@Override
	public boolean reset() throws AlgorithmException{
		myConvertedGrammar = new Grammar();
		
		GroupingPair gp = SpecialSymbolFactory.getBestGrouping(myAutomaton.getInputAlphabet());
		
		if (gp == null) 
			return false;
		
		myConvertedGrammar.setVariableGrouping(gp);
		
		myMappedVariables = new HashMap<S, Variable>();
		myConvertedTransitions = new HashSet<Transition>();
		
		return true;
	}

	public S[] getUnmappedMappings(){
		HashSet<S> all = new HashSet<S>(allMappings);
		all.removeAll(this.myMappedVariables.keySet());
		return (S[]) all.toArray();
	}

	public E[] getUnconvertedTransitions(){
		HashSet<E> all = new HashSet<E>(this.getAutomaton().getTransitions());
		all.removeAll(this.myConvertedTransitions);
		return (E[]) all.toArray();
	}
	
	
	
	public boolean allTransitionsConverted() {
		return getUnconvertedTransitions().length == 0;
	}

	public  boolean variableMappingsComplete(){
		return this.getUnmappedMappings().length == 0;
	}

	public abstract boolean doFinalSteps();

	public abstract boolean isStartMapping(S mapping);

	public abstract Production[] convertTransition(E trans);

	public abstract Set<S> getAllNecessaryMappings(T automaton);

	public abstract BooleanWrapper checkOfProperForm(T automaton);


}
