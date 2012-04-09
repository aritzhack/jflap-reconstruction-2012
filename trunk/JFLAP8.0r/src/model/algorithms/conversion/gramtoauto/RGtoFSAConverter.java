package model.algorithms.conversion.gramtoauto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;
import model.formaldef.components.alphabets.symbols.Variable;
import model.formaldef.rules.GroupingRule;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.typetest.GrammarType;

public class RGtoFSAConverter extends GrammarToAutomatonConverter<FiniteStateAcceptor, FiniteStateTransition> {

	private State myFinalState;

	private static final String DEFAULT_NAME = "qF";

	public RGtoFSAConverter(Grammar g) {
		super(g);
	}

	@Override
	public String getDescriptionName() {
		return "Regular Grammar to Finite state accepter";
	}

	@Override
	public String getDescription() {
		return "Converts a regular grammar to a finite state accepter";
	}

	@Override
	public FiniteStateAcceptor createEmptyAutomaton() {
		return new FiniteStateAcceptor();
	}

	
	
	@Override
	public FiniteStateTransition convertProduction(Production p) {
		Variable lhsVar = (Variable) p.getLHS().getFirst();
		SymbolString rhs = p.getRHS();
		
		State from = this.getStateForVariable(lhsVar),
				to;
		
		SymbolString input;
		
		if (p.getRHS().isEmpty() || 
				!(p.getRHS().getLast() instanceof Variable)){
			to = myFinalState;
			input = p.getRHS();
		}
		else {
			to = this.getStateForVariable((Variable) p.getRHS().getLast());
			input = rhs.subList(0, rhs.size()-1);
		}
		return new FiniteStateTransition(from, to, input);
	}

	
	
	public State getFinalState() {
		return myFinalState;
	}

	private State getStateForVariable(Variable first) {
		for (State s: this.getConvertedAutomaton().getStates()){
			if (s.getName().equals(createStateString(first)))
				return s;
		}
		return null;
	}

	private String createStateString(Variable v) {
		String name = v.toString();
		
		if (this.getGrammar().usingGrouping()){
			name = name.substring(1,name.length()-1);
		}
		return name;
	}

	@Override
	public Set<State> createAutomaticStates() {
		Set<State> states = new HashSet<State>();
		int id = 0;
		for (Symbol v: this.getGrammar().getVariables()){
			
			String name = createStateString((Variable) v);
			State temp = new State(name, id, null);
			states.add(temp);
			id++;
			//check and add star state;
			if (v.equals(this.getGrammar().getStartVariable())){
				this.getConvertedAutomaton().getStartState().setTo(temp);
			}
		}

		return states;
	}

	public boolean createAndAddFinalState(String name){
		int id = this.getConvertedAutomaton().getStates().getNextID();
		myFinalState = new State(name, id, null);
		boolean added = this.getConvertedAutomaton().getStates().add(myFinalState)
				&& this.getConvertedAutomaton().getFinalStateSet().add(myFinalState);
		return added;
	}
	
	@Override
	public AlgorithmStep[] initializeAllSteps() {
		List<AlgorithmStep> steps = new ArrayList<AlgorithmStep>();
		steps.addAll(Arrays.asList(super.initializeAllSteps()));
		steps.add(2, new AddFinalState());
		return steps.toArray(new AlgorithmStep[0]);
	}
	
	@Override
	public GrammarType[] getValidTypes() {
		return new GrammarType[]{GrammarType.RIGHT_LINEAR};
	}

	/////////////////////////////////////////////////
	////////////// Algorithm Steps //////////////////
	/////////////////////////////////////////////////
	
	private class AddFinalState implements AlgorithmStep {

		@Override
		public String getDescriptionName() {
			return "Add Final State";
		}

		@Override
		public String getDescription() {
			return "Adds a final state to this FSA. This is not linked" +
					" to any variable.";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			int id = getConvertedAutomaton().getStates().getNextID();
			return createAndAddFinalState(DEFAULT_NAME + id);
		}

		@Override
		public boolean isComplete() {
			return getFinalState() != null;
		}
		
		
	}
	
	
}
