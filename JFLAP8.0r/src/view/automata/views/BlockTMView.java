package view.automata.views;

import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;

public class BlockTMView extends AutomataView<BlockTuringMachine, BlockTransition>{

	public BlockTMView(BlockTuringMachine model) {
		super(model);
	}

}
