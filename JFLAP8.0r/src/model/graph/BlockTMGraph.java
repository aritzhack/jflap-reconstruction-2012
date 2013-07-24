package model.graph;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import model.automata.State;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.graph.layout.GEMLayoutAlgorithm;

public class BlockTMGraph extends TransitionGraph<BlockTransition> {

	private Map<Block, TransitionGraph> blockGraphs;

	public BlockTMGraph(BlockTuringMachine a) {
		this(a, new GEMLayoutAlgorithm());
	}

	public BlockTMGraph(BlockTuringMachine a, LayoutAlgorithm alg) {
		super(a, alg);
		blockGraphs = new HashMap<Block, TransitionGraph>();
		BlockSet blocks = a.getStates();

		for (State s : blocks) {
			Block b = (Block) s;
			setGraph(b, new TransitionGraph(b.getTuringMachine()));
		}
	}

	@Override
	public BlockTuringMachine getAutomaton() {
		return (BlockTuringMachine) super.getAutomaton();
	}

	public void setGraph(Block b, TransitionGraph transitionGraph) {
		TransitionGraph current = blockGraphs.get(b);
		blockGraphs.put(b, transitionGraph);
		distributeChanged();
	}

	public TransitionGraph getGraph(Block b) {
		return blockGraphs.get(b);
	}

	@Override
	public boolean addVertex(State vertex, Point2D point) {
		Block b = (Block) vertex;
		if (super.addVertex(b, point) && blockGraphs != null) {
			setGraph(b, new TransitionGraph(b.getTuringMachine()));
			return true;
		}
		return false;
	}

	@Override
	public boolean removeVertex(State vertex) {
		Block b = (Block) vertex;
		if (super.removeVertex(b) && blockGraphs != null) {
			blockGraphs.remove(b);
			return true;
		}
		return false;
	}

}
