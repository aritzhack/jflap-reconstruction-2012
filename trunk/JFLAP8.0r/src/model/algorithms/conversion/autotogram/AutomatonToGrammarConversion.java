package model.algorithms.conversion.autotogram;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.algorithms.conversion.ConversionAlgorithm;
import model.automata.Automaton;
import model.automata.SingleInputTransition;
import model.automata.Transition;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.components.alphabets.grouping.SpecialSymbolFactory;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import errors.BooleanWrapper;

public abstract class AutomatonToGrammarConversion<T extends Automaton<E>, 
														 S extends VariableMapping, 
															E extends Transition<E>> 
																			extends ConversionAlgorithm<T, Grammar> {
	/**
	 * The {@link Map} of {@link VariableMapping} to {@link Variable}
	 * that have already been added.
	 */
	private Map<S, Variable> myMappedVariables;

	/**
	 * The set of {@link SingleInputTransition} from the {@link Automaton} that
	 * have already been converted to productions.
	 */
	private HashSet<Transition<E>> myConvertedTransitions;

	
	
	public AutomatonToGrammarConversion(T automaton) throws AlgorithmException{
		super(automaton);
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
		return getConvertedDefinition();
	}
	
	public T getAutomaton(){
		return getOriginalDefinition();
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
			if (bw.isError())
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
		boolean added = getConvertedGrammar().getVariables().add(var);
		
		if (added && isStartMapping(mapping)){
//			System.out.println("|" + myConvertedGrammar.getStartVariable().getString() + "|");
			if(getConvertedGrammar().getStartVariable() != null)
				throw new AlgorithmException("A Start Variable mapping has already been added " +
						"to the Converted grammar.");
			getConvertedGrammar().setStartVariable(var);
		}
		
		return new BooleanWrapper (added, 
							"Could not add the variable " + var + "to the converted Grammar.");
	}

	public boolean convertInputAlphabet() {
		boolean converted = true;
		for (Symbol s: getAutomaton().getInputAlphabet()){
			converted = converted && getConvertedGrammar().getTerminals().add(new Terminal(s.toString()));
		}
		return converted;
	}

	public boolean inputAlphabetConverted() {
		return getConvertedGrammar().getTerminals().size() == getAutomaton().getInputAlphabet().size();
	}

	@Override
	public boolean reset() throws AlgorithmException{
		myMappedVariables = new HashMap<S, Variable>();
		myConvertedTransitions = new HashSet<Transition<E>>();
		return super.reset();
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
	
	@Override
	public Grammar createBaseConverted() {
		Grammar g = new Grammar();
		GroupingPair gp = SpecialSymbolFactory.getBestGrouping(getAutomaton().getInputAlphabet());
		g.setVariableGrouping(gp);
		return g;
	}
	
	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{new ConvertInputAlphabet(),
									new MapAllVariables(),
									new ConvertTransitions()};
	}

	public abstract boolean isStartMapping(S mapping);

	public abstract Production[] convertTransition(E trans);

	public abstract Set<S> getAllNecessaryMappings();

	
	
	
	/////////////////////////////////////////////////
	////////////// Algorithm Steps //////////////////
	/////////////////////////////////////////////////
	
	/**
	 * Converts the Input Alphabet of the Automaton
	 * to the Terminal alphabet of the Grammar. First
	 * step in the conversion algorithm
	 * 
	 * @author Julian
	 *
	 */
	private class ConvertInputAlphabet implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Convert Input Alphabet";
		}

		@Override
		public String getDescription() {
			return "Converts the Input Alphabet of the Automaton" +
					" to the Terminal alphabet of the Grammar.";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return convertInputAlphabet();
		}

		@Override
		public boolean isComplete() {
			return inputAlphabetConverted();
		}
		
	}
	
	/**
	 * Create all variable mappings and then make sure those
	 * are mapped to variables which will compose the grammar
	 * variable alphabet. This will perform automatic variable
	 * naming as determined by the specific converter.;
	 * @author Julian
	 *
	 */
	private class MapAllVariables implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Map all variables";
		}

		@Override
		public String getDescription() {
			return "Create all variable mappings and then make sure those" +
					" are mapped to variables which will compose the grammar" +
					" variable alphabet.";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return doAllAutomaticVariableMappings();
		}

		@Override
		public boolean isComplete() {
			return doAllAutomaticVariableMappings();
		}
		
	}
	
	/**
	 * Converts all of the transitions in the associated
	 * automaton into productions in this grammar. Depending
	 * on the behavior of this algorithm, the transitions
	 * will be transformed into the appropriate kind of production.
	 * @author Julian
	 *
	 */
	private class ConvertTransitions implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Covert Transitions to Productions.";
		}

		@Override
		public String getDescription() {
			return "Converts all of the transition functions of" +
					" the automaton to productions in the grammar.";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return convertRemainingTransitions();
		}

		@Override
		public boolean isComplete() {
			return allTransitionsConverted();
		}
		
	}
	

}
