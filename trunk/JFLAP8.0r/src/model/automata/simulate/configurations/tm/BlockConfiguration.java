package model.automata.simulate.configurations.tm;

import java.util.List;

import util.JFLAPConstants;
import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.simulate.AutoSimulator;
import model.automata.simulate.Configuration;
import model.automata.simulate.ConfigurationChain;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class BlockConfiguration extends TMConfiguration<BlockTuringMachine, BlockTransition> {

	private SymbolString myUpdatedTape;

	public BlockConfiguration(BlockTuringMachine tm, State s, int pos,
			SymbolString tape) {
		super(tm, s, new int[]{pos}, tape);
	}

	@Override
	protected Configuration<BlockTuringMachine, BlockTransition> createConfig(
			BlockTuringMachine a, State s, int ppos, SymbolString primary,
			int[] positions, SymbolString[] updatedClones) throws Exception {
		return new BlockConfiguration(a, s, positions[0], updatedClones[0]);
	}

	@Override
	protected boolean canMoveAlongTransition(BlockTransition trans) {
		Symbol read = this.getReadForTape(0);
		Symbol[] input = trans.getInput();
		if (input.length > 1)
			return !read.equals(input[1]);
		if (!input[0].equals(JFLAPConstants.TILDE)||
				!read.equals(input[0]))
			return false;
		myUpdatedTape = applyBlock(trans.getToState());
		return myUpdatedTape != null;
	}

	/**
	 * Applies this block to the configuration
	 * @param toState
	 * @return
	 */
	private SymbolString applyBlock(Block toState) {
		AutoSimulator auto = new AutoSimulator(toState.getTuringMachine(),
				getSpecialCase());
		auto.beginSimulation(getStringForIndex(0).copy());
		List<ConfigurationChain> chainList = auto.getNextAccept();
		if (chainList.isEmpty()) return null;
		
		ConfigurationChain chain = chainList.get(0);
		return chain.getLast().getStringForIndex(0);
	}

	@Override
	protected int getNextSecondaryPosition(int i, BlockTransition trans) {
		return getPositionForIndex(i);
	}

	@Override
	protected SymbolString[] assembleUpdatedStrings(SymbolString[] clones,
			BlockTransition trans) {
		return new SymbolString[]{myUpdatedTape};
	}

}
