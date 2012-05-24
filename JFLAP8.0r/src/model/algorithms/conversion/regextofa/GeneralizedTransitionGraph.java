package model.algorithms.conversion.regextofa;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.regex.RegularExpression;

public class GeneralizedTransitionGraph extends FiniteStateAcceptor {

	private RegularExpression myRegEx;

	public GeneralizedTransitionGraph(RegularExpression regex) {
		myRegEx = regex;
		this.getInputAlphabet().addAll(myRegEx.getInputAlphabet());
		this.getInputAlphabet().addAll(myRegEx.getOperators());

		State start = this.getStates().createAndAddState();
		State end = this.getStates().createAndAddState();

		this.setStartState(start);
		this.getFinalStateSet().add(end);

		FiniteStateTransition trans = new FiniteStateTransition(start, 
				end, 
				myRegEx.getExpression());
		this.getTransitions().add(trans);
	}

	@Override
	public String getDescriptionName() {
		return "Generalized Transition Graph";
	}

	public FiniteStateAcceptor createNFAFromGTG(){
		StateSet states = (StateSet) this.getStates().copy();
		InputAlphabet inputAlph = (InputAlphabet) myRegEx.getInputAlphabet().copy();
		TransitionFunctionSet<FiniteStateTransition> transitions = 
				(TransitionFunctionSet<FiniteStateTransition>) this.getTransitions().copy();
		StartState start = new StartState(stateHelper(states, 
				this.getStartState().getID()));
		FinalStateSet finalStates = new FinalStateSet();
		for (State s: this.getFinalStateSet()){
			finalStates.add(stateHelper(states, s.getID()));
		}
		FiniteStateAcceptor nfa = new FiniteStateAcceptor(states, 
				inputAlph, 
				transitions, 
				start, 
				finalStates);

		return nfa;
	}

	public State stateHelper(StateSet states, int id) {
		return states.getStateWithID(id);
	}
}
