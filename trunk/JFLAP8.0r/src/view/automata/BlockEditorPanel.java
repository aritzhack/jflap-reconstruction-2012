package view.automata;

import java.awt.Point;

import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.undo.UndoKeeper;

public class BlockEditorPanel extends
		AutomatonEditorPanel<BlockTuringMachine, BlockTransition> {

	public BlockEditorPanel(BlockTuringMachine m, UndoKeeper keeper,
			boolean editable) {
		super(m, keeper, editable);
		// TODO Auto-generated constructor stub
	}
	
	public Block addBlock(Block b, Point p){
		BlockSet blocks = getAutomaton().getStates();
		if(blocks.getStateWithID(b.getID()) != null)
			b = b.copy(blocks.getNextUnusedID());
		blocks.add(b);
		moveState(b, p);
		return b;
	}

}
