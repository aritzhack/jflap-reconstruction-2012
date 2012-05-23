package model.automata.simulate.configurations;

import java.util.AbstractCollection;

import model.automata.State;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.simulate.SingleInputSimulator;
import model.formaldef.components.symbols.SymbolString;

public class PDAConfiguration extends
		SingleSecondaryConfiguration<PushdownAutomaton, PDATransition> {

	public PDAConfiguration(PushdownAutomaton a, State s, int pos, SymbolString input,
			SymbolString stack) {
		super(a, s, pos, input, stack);
	}

	@Override
	public String getName() {
		return "PDA Configuration";
	}

	private SymbolString getStack(){
		return this.getStringForIndex(0);
	}

	private boolean canPop(SymbolString pop) {
		return this.getStack().startsWith(pop);
	}

	@Override
	protected boolean canMoveAlongTransition(
			PDATransition trans) {
		return super.canMoveAlongTransition(trans) && this.canPop(trans.getPop());
	}

	@Override
	protected boolean isInFinalState() {
		if (this.getSpecialCase() == SingleInputSimulator.DEFAULT)
			return super.isInFinalState();
		return this.getStack().isEmpty();
	}

	

	@Override
	protected boolean shouldFindValidTransitions() {
		return true;
	}

	@Override
	protected String getSecondaryName() {
		return "Stack";
	}

	@Override
	protected int getPositionChange(PDATransition trans) {
		return trans.getPush().size()-trans.getPop().size();
	}

	@Override
	protected SymbolString createUpdatedSecondary(SymbolString clone,
			PDATransition trans) {
		SymbolString stack = clone;
		SymbolString pop = trans.getPop();
		SymbolString push = trans.getPush().clone();
		return push.concat(stack.subList(pop.size()));
	}

}
