package model.automata.acceptors.pda;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.symbols.BottomOfStackSymbol;

public class PushdownAutomaton extends Acceptor<PDATransition> {

	private BottomOfStackSymbol myBottomOfStackSymbol;
	private StackAlphabet myStackAlphabet;

	public PushdownAutomaton(StateSet states, 
								InputAlphabet inputAlph,
								StackAlphabet stackAlph,
								TransitionFunctionSet<PDATransition> functions, 
								StartState start,
								BottomOfStackSymbol bottom,
								FinalStateSet finalStates) {
		super(states, inputAlph, functions, start, finalStates);
		myStackAlphabet = stackAlph;
		myBottomOfStackSymbol = bottom;
	}
	
	@Override
	public String getDescriptionName() {
		return "Pushdown Automaton (PDA)";
	}

	@Override
	public String getDescription() {
		return "An variety of automaton, more complex than a finite state automaton. " +
				"Uses a single stack as memory, allowinf for more complex languages. " +
				"The kind of language defined by a PDA is a Context Free Language (CFL).";
	}

	@Override
	public PushdownAutomaton alphabetAloneCopy() {
		return new PushdownAutomaton(new StateSet(),
										this.getInputAlphabet(), 
										this.getStackAlphabet(), 
										new TransitionFunctionSet<PDATransition>(), 
										new StartState(), 
										new BottomOfStackSymbol(), 
										new FinalStateSet());
	}

	@Override
	public FormalDefinitionComponent[] getComponents() {
			return new FormalDefinitionComponent[]{this.getStates(),
														this.getInputAlphabet(),
														this.getStackAlphabet(),
														this.getTransitions(),
														this.getStartState(),
														this.getBottomOfStackSymbol(),
														this.getFinalStateSet()};
	}

	public BottomOfStackSymbol getBottomOfStackSymbol() {
		return myBottomOfStackSymbol;
	}

	public StackAlphabet getStackAlphabet() {
		return myStackAlphabet;
	}

	
	
}
