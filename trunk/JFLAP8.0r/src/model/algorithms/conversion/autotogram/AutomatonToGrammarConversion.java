package model.algorithms.conversion.autotogram;

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
import model.formaldef.components.alphabets.symbols.SymbolString;
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
		BooleanWrapper[] bw = automaton.isComplete();
		if (bw.length > 0){
			throw new AlgorithmException(bw);
		}
		bw = checkOfProperForm(automaton);
		if (bw.length > 0)
			throw new AlgorithmException(bw);
		if (!this.reset())
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

		return false;
		
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
		myConvertedTransitions.add(trans);
		return this.getConvertedGrammar().getProductionSet().addAll(Arrays.asList(p));
	}

	public boolean doAllAutomaticVariableMappings(){
		for (S mapping: this.getUnmappedMappings()){
			Variable auto = new Variable(mapping.toString());
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
//			System.out.println("|" + myConvertedGrammar.getStartVariable().getString() + "|");
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

	public Set<S> getUnmappedMappings(){
		Set<S> all = getAllNecessaryMappings();
		all.removeAll(this.myMappedVariables.keySet());
		return all;
	}

	public Set<E> getUnconvertedTransitions(){
		HashSet<E> all = new HashSet<E>(this.getAutomaton().getTransitions());
		all.removeAll(this.myConvertedTransitions);
		return all;
	}
	
	public boolean allTransitionsConverted() {
		return getUnconvertedTransitions().isEmpty();
	}

	public  boolean variableMappingsComplete(){
		return this.getUnmappedMappings().isEmpty();
	}
	
	public Variable getVarForMapping(S mapping){
		return myMappedVariables.get(mapping);
	}

	protected SymbolString convertToTerminals(SymbolString input) {
		SymbolString terms = new SymbolString();
		
		for (Symbol s : input){
			terms.add(new Terminal(s.toString()));
		}
		
		
		return terms;
	}
	
	
	public abstract boolean doFinalSteps();

	public abstract boolean isStartMapping(S mapping);

	public abstract Production[] convertTransition(E trans);

	public abstract Set<S> getAllNecessaryMappings();

	/**
	 * Checks to see if the automaton to be converted is
	 * of the proper form for this algorithm. <code>isComplete()</code>
	 * has already been checked in the constructor.
	 * @param automaton
	 * @return
	 */
	public abstract BooleanWrapper[] checkOfProperForm(T automaton);


}
