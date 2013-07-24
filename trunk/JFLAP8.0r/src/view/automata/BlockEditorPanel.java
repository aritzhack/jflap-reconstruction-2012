package view.automata;

import java.awt.Point;

import debug.JFLAPDebug;

import model.automata.State;
import model.automata.turing.TuringMachine;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.graph.BlockTMGraph;
import model.graph.TransitionGraph;
import model.undo.UndoKeeper;

public class BlockEditorPanel extends
		AutomatonEditorPanel<BlockTuringMachine, BlockTransition> {

	public BlockEditorPanel(BlockTuringMachine m, UndoKeeper keeper,
			boolean editable) {
		super(m, keeper, editable);
		setGraph(new BlockTMGraph(m));
	}
	
	public Block addBlock(Block b, Point p){
		BlockSet blocks = getAutomaton().getStates();
		if(blocks.getStateWithID(b.getID()) != null)
			b = b.copy(blocks.getNextUnusedID());
		blocks.add(b);
		moveState(b, p);
		return b;
	}
	
	public TransitionGraph getGraph(Block b){
		return getGraph().getGraph(b);
	}
	
	public void setGraph(TuringMachine machine, TransitionGraph graph){
		for(State s : getAutomaton().getStates()){
			Block b = (Block) s;
			
			if(b.getTuringMachine().equals(machine)){
				getGraph().setGraph(b, graph);
				return;
			}
		}
	}

	@Override
	public BlockTMGraph getGraph() {
		return (BlockTMGraph) super.getGraph();
	}
}
