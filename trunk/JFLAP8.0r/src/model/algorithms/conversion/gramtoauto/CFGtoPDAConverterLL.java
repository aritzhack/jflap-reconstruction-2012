package model.algorithms.conversion.gramtoauto;

import java.util.Arrays;

import model.automata.State;
import model.automata.Transition;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PDATransition;
import model.formaldef.components.alphabets.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.StartVariable;

public class CFGtoPDAConverterLL extends CFGtoPDAConverter {

	public CFGtoPDAConverterLL(Grammar g) {
		super(g);
	}

	@Override
	public String getSubtype() {
		return "LL";
	}

	@Override
	public PDATransition convertProduction(Production p) {
		State focus = this.getMiddleState();
		SymbolString input = new SymbolString(),
						pop = p.getLHS(),
						push = p.getRHS();
		
		return new PDATransition(focus, focus, input, pop, push);
	}

	@Override
	protected boolean setUpTransitions() {
		BottomOfStackSymbol bos = this.getConvertedAutomaton().getBottomOfStackSymbol();
		StartVariable start = this.getGrammar().getStartVariable();
		TransitionFunctionSet<PDATransition> transitions = this.getConvertedAutomaton().getTransitions();
		
		
		PDATransition initial = new PDATransition(getStartState(),
													getMiddleState(), 
													new SymbolString(),
													new SymbolString(bos), 
													new SymbolString(start, bos));
		
		PDATransition toFinal = new PDATransition(getMiddleState(), 
													getFinalState(),
													new SymbolString(), 
													new SymbolString(bos), 
													new SymbolString());
		
		transitions.add(initial);
		transitions.add(toFinal);
		
		
		PDATransition[] loops = createAllReduceLoops(this.getGrammar().getTerminals(), 
													this.getMiddleState());
		
		return transitions.addAll(Arrays.asList(loops));
	}
	

}
