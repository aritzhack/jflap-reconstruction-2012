package model.algorithms.conversion.gramtoauto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.SingleInputTransition;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.acceptors.pda.StackAlphabet;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.grouping.SpecialSymbolFactory;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;
import model.grammar.typetest.GrammarType;

public abstract class CFGtoPDAConverter extends GrammarToAutomatonConverter<PushdownAutomaton, PDATransition> {

	private State myStartState;
	private State myMiddleState;
	private State myFinalState;

	public CFGtoPDAConverter(Grammar g) {
		super(g);
	}

	@Override
	public String getDescriptionName() {
		return "CFG to PDA Converter " + "(" + this.getSubtype() + ")";
	}

	@Override
	public String getDescription() {
		return "Converts a context-free grammar into a deterministic pushdown automaton.";
	}

	@Override
	public PushdownAutomaton createEmptyAutomaton() {
		return new PushdownAutomaton();
	}

	
	
	@Override
	public boolean convertAlphabets() {
		
		boolean terminalsConverted = super.convertAlphabets();
		
		if (!terminalsConverted) return false;
		
		return doStackAlphabet();
	}

	private boolean doStackAlphabet() {
		Symbol[] terms = this.getGrammar().getTerminals().toArray(new Symbol[0]);
		Symbol[] vars = this.getGrammar().getVariables().toArray(new Symbol[0]);
		StackAlphabet stackAlph = getConvertedAutomaton().getStackAlphabet();
		return Alphabet.addCopiedSymbols(stackAlph,terms) &&
				Alphabet.addCopiedSymbols(stackAlph,vars);
	}
	
	

	@Override
	public boolean alphabetsConverted() {
		return super.alphabetsConverted() && stackAlphConverted();
	}

	private boolean stackAlphConverted() {
		StackAlphabet stackAlph = getConvertedAutomaton().getStackAlphabet();
		TerminalAlphabet terms = this.getGrammar().getTerminals();
		VariableAlphabet vars =  this.getGrammar().getVariables();
		return stackAlph.size() >= (terms.size() + vars.size());
	}

	@Override
	public GrammarType[] getValidTypes() {
		return new GrammarType[]{GrammarType.CONTEXT_FREE};
	}


	@Override
	public boolean doSetup() {
		StateSet stateSet = this.getConvertedAutomaton().getStates();
		boolean success = true;
		
		//set up start state;
		myStartState = new State(null, stateSet.getNextUnusedID());
		success &= stateSet.add(myStartState);
		this.getConvertedAutomaton().setStartState(myStartState);

		
		myMiddleState = new State(null, stateSet.getNextUnusedID());
		success &= stateSet.add(myMiddleState);
		
		//set up final state
		myFinalState = new State(null, stateSet.getNextUnusedID());
		FinalStateSet finalStates = this.getConvertedAutomaton().getFinalStateSet();
		success &= stateSet.add(myFinalState);
		success &= finalStates.add(myFinalState);
		
		//Add all of these states to the automaton
		Symbol symbol = 
				SpecialSymbolFactory.getReccomendedBOSSymbol(this.getConvertedAutomaton().getStackAlphabet());
		this.getConvertedAutomaton().setBottomOfStackSymbol(symbol);
		
		return success && this.setUpTransitions();
	}

	public State getStartState(){
		return myStartState;
	}
	
	public State getMiddleState(){
		return myMiddleState;
	}
	
	public State getFinalState(){
		return myFinalState;
	}
	
	public abstract String getSubtype();

	protected abstract boolean setUpTransitions();

}
