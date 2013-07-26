package model.graph;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.TreeMap;

import model.automata.State;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.automata.turing.TuringMachine;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.graph.layout.GEMLayoutAlgorithm;

public class BlockTMGraph extends TransitionGraph<BlockTransition> {

	private Map<Integer, TransitionGraph> blockGraphs;

	public BlockTMGraph(BlockTuringMachine a) {
		this(a, new GEMLayoutAlgorithm());
	}

	public BlockTMGraph(BlockTuringMachine a, LayoutAlgorithm alg) {
		super(a, alg);
		blockGraphs = new TreeMap<Integer, TransitionGraph>();
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
		TransitionGraph current = blockGraphs.get(b.getID());
		if (current == null || !current.getClass().equals(transitionGraph.getClass()))
			blockGraphs.put(b.getID(), transitionGraph);
		else {
			TuringMachine auto = b.getTuringMachine();
			TransitionSet<? extends Transition<?>> transitions = auto
					.getTransitions();

			for (State s : auto.getStates())
				current.moveVertex(s, transitionGraph.pointForVertex(s));
			for (Transition t : transitions)
				current.setControlPt(transitionGraph.getControlPt(t), t);
		}
		// blockGraphs.put(b.getID(), transitionGraph);
		distributeChanged();
	}

	public TransitionGraph getGraph(Block b) {
		return blockGraphs.get(b.getID());
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
			blockGraphs.remove(b.getID());
			return true;
		}
		return false;
	}

}
