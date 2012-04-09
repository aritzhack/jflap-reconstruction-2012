package model.algorithms.conversion.gramtoauto;

import java.util.Set;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.typetest.GrammarType;

public abstract class CFGtoPDAConverter extends GrammarToAutomatonConverter<PushdownAutomaton, PDATransition> {

	public CFGtoPDAConverter(Grammar g) {
		super(g);
	}

	@Override
	public String getDescriptionName() {
		return "CFG to PDA Converter " + "(" + this.getSubtype() + ")";
	}

	public abstract String getSubtype();

	@Override
	public String getDescription() {
		return "Converts a context-free grammar into a deterministic pushdown automaton.";
	}

	@Override
	public PushdownAutomaton createEmptyAutomaton() {
		return new PushdownAutomaton();
	}

	@Override
	public GrammarType[] getValidTypes() {
		return new GrammarType[]{GrammarType.CONTEXT_FREE};
	}

	@Override
	public PDATransition convertProduction(Production p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<State> createAutomaticStates() {
		// TODO Auto-generated method stub
		return null;
	}


}
