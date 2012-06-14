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
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.Transition;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FSATransition;
import model.change.rules.GroupingRule;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.StartVariable;
import model.grammar.typetest.GrammarType;

public class RGtoFSAConverter extends GrammarToAutomatonConverter<FiniteStateAcceptor, FSATransition> {

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
	public FSATransition convertProduction(Production p) {
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
		return new FSATransition(from, to, input);
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
	public boolean doSetup() {
		
		StateSet states = this.getConvertedAutomaton().getStates();
		State startState = this.getConvertedAutomaton().getStartState();
		Variable startVar = this.getGrammar().getStartVariable().toSymbolObject();
		FinalStateSet finalStates = this.getConvertedAutomaton().getFinalStateSet();
		
		//do all non-final states
		boolean success = setupStates(states, startState, startVar);
		
		//do final states
		success &= setUpFinalStates(finalStates, 
							states);
		
		
		return success;
	}

	private boolean setupStates(StateSet states, 
									State startState,
									Variable startVar) {
		for (Symbol v: this.getGrammar().getVariables()){
			int id = states.getNextUnusedID();
			String name = createStateString((Variable) v);
			
			State newState = new State(name, id);
			
			boolean added = states.add(newState);
			
			if (!added) return false;
			
			//if it is the start variable, set the corresponding
				//state to start state
			if (startVar.equals(v)){
				this.getConvertedAutomaton().setStartState(newState);
			}
			
		}
		
		return true;
	}

	private boolean setUpFinalStates(FinalStateSet finalStates, StateSet states) {
		int id = states.getNextUnusedID();
		String name = DEFAULT_NAME + id;
		myFinalState = new State(name, id);
		boolean added = states.add(myFinalState)
				&& finalStates.add(myFinalState);
		return added;
	}

	
	@Override
	public GrammarType[] getValidTypes() {
		return new GrammarType[]{GrammarType.RIGHT_LINEAR};
	}

	
}
