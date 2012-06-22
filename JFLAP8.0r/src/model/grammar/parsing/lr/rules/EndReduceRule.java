package model.grammar.parsing.lr.rules;

import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.grammar.Variable;
import model.grammar.parsing.lr.SLR1DFAState;

public class EndReduceRule extends StateUsingRule {


	public EndReduceRule(State to) {
		super(to);
	}

	@Override
	public String getDescriptionName() {
		return "Finish Reduce Rule";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return ""+ getToState().getID();
	}

}
