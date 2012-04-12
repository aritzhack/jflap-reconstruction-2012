package model.algorithms.conversion.gramtoauto;

import java.security.AllPermission;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.algorithms.SteppableAlgorithm;
import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.StateSet;
import model.automata.Transition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.typetest.GrammarType;

public abstract class GrammarToAutomatonConverter<T extends Automaton<S>, S extends Transition> 
																		extends SteppableAlgorithm {

	
	private Grammar myGrammar;
	
	
	private T myAutomaton;
	
	
	private ProductionSet myConvertedProductions;


	private Set<State> myAutoStates;

	
	public GrammarToAutomatonConverter(Grammar g){
		setGrammar(g);
	}
	
	public void setGrammar(Grammar g) {
		if (!canConvert(g))
			throw new AlgorithmException("Cannot convert a grammar of" +
					"type " + GrammarType.getType(g) + " with " + this.getClass().getName());
		myGrammar= g;
		reset();
	}

	public boolean canConvert(Grammar g) {
		
		for (GrammarType gt: GrammarType.getType(g)){
			if (Arrays.asList(this.getValidTypes()).contains(gt))
					return true;
		}
		
		return false;
	}

	public boolean convertAlphabets() {
		Symbol[] terms = this.getGrammar().getTerminals().toArray(new Symbol[0]);
		InputAlphabet inAlph = getConvertedAutomaton().getInputAlphabet();
		return Alphabet.addCopiedSymbols(inAlph,terms);
	}
	
	public boolean alphabetsConverted() {
		return this.getGrammar().getTerminals().size() == 
				this.getConvertedAutomaton().getInputAlphabet().size();
	}
	
	
	public Grammar getGrammar() {
		return myGrammar;
	}

	public T getConvertedAutomaton() {
		return myAutomaton;
	}

	public boolean convertAllProductions(){
		for (Production p : this.getUnconvertedProductions()){
			if(!this.convertAndAddProduction(p))
				return false;
		}
		return true;
	}
	
	private boolean convertAndAddProduction(Production p) {
		S trans = this.convertProduction(p);
		boolean success = trans != null && 
				this.getConvertedAutomaton().getTransitions().add(trans);
		return success;
	}

	public Set<Production> getUnconvertedProductions(){
		HashSet<Production> all = new HashSet<Production>(this.getGrammar().getProductionSet());
		all.removeAll(this.getConvertedProductions());
		return all;
	}
	
	
	public Set<Production> getConvertedProductions() {
		return myConvertedProductions;
	}

	public boolean allProductionsConverted() {
		return getUnconvertedProductions().isEmpty();
	}

	public boolean autoStatesAdded() {
		StateSet states = this.getConvertedAutomaton().getStates();
		return myAutoStates != null && states.containsAll(myAutoStates);
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{
				new ConvertAlphabets(),
				new ConvertProductions()};
	}


	@Override
	public boolean reset() throws AlgorithmException {
		myAutomaton = createEmptyAutomaton();
		myConvertedProductions = new ProductionSet();
		
		return doSetup();
	}



	public abstract boolean doSetup();

	public abstract T createEmptyAutomaton();


	public abstract GrammarType[] getValidTypes();
	
	
	public abstract S convertProduction(Production p);
	
	
	
	/////////////////////////////////////////////////
	////////////// Algorithm Steps //////////////////
	/////////////////////////////////////////////////
	
	/**
	 *  Convert all of the terminals in the terminal alphabet
	 *  of the grammar into symbols in the input alphabet of
	 *  the automaton.
	 * @author Julian
	 *
	 */
	private class ConvertAlphabets implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Convert Terminals to Input Alphabet";
		}

		@Override
		public String getDescription() {
			return "Convert all of the terminals in the terminal alphabet" +
					" of the grammar into symbols in the input alphabet of" +
					" the automaton";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return convertAlphabets();
		}

		@Override
		public boolean isComplete() {
			return alphabetsConverted();
		}
		
	}

	
	/**
	 * Converts all productions i nthe grammar into transition
	 * functions in the automaton. This is executed via the 
	 * specific implementation in the {@link GrammarToAutomatonConverter}
	 * subclass.
	 * 
	 * @author Julian
	 *
	 */
	private class ConvertProductions implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Convert Productions";
		}

		@Override
		public String getDescription() {
			return "Converts all productions into transitions in" +
					" the automaton.";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return convertAllProductions();
		}

		@Override
		public boolean isComplete() {
			return allProductionsConverted();
		}
		
	}
	
}

