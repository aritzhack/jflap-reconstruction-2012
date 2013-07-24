package file.xml.graph;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.automata.State;
import model.automata.Transition;
import model.automata.turing.TuringMachine;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.graph.BlockTMGraph;
import model.graph.TransitionGraph;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import debug.JFLAPDebug;
import file.xml.StructureTransducer;
import file.xml.TransducerFactory;
import file.xml.XMLHelper;
import file.xml.XMLTransducer;

public class BlockTMGraphTransducer extends TransitionGraphTransducer {

	private static final String SUBGRAPH_TAG = "subgraph";

	@Override
	public String getTag() {
		return BLOCK_GRAPH_TAG;
	}
	
	@Override
	public BlockTMGraph fromStructureRoot(
			Element root) {
		TransitionGraph<?> graph = super.fromStructureRoot(root);
		BlockTuringMachine machine = (BlockTuringMachine) graph.getAutomaton();
		BlockTMGraph blockGraph = new BlockTMGraph(machine);
		
		Element sub_elem = XMLHelper.getChildrenWithTag(root, SUBGRAPH_TAG).get(0);
		List<Element> turings = XMLHelper.getChildrenWithTag(sub_elem, STRUCTURE_TAG);
		Map<TuringMachine, TransitionGraph> graphMap = new HashMap<TuringMachine, TransitionGraph>();
		
		for (Element block_elem : turings){
			String type = StructureTransducer.retrieveTypeTag(block_elem);
			
			if(type.equals(getTag()) || type.equals(TRANS_GRAPH_TAG)){
				XMLTransducer<TransitionGraph> subTrans = TransducerFactory.getTransducerForTag(type);
				TransitionGraph subGraph = subTrans.fromStructureRoot(block_elem);
				
				graphMap.put((TuringMachine) subGraph.getAutomaton(), subGraph);
			}
		}
		
		BlockSet blocks = machine.getStates();
		
		for (State s : blocks) {
			Block b = (Block) s;
			blockGraph.moveVertex(s, graph.pointForVertex(s));
			TuringMachine tm = b.getTuringMachine();
			for(TuringMachine machine1 : graphMap.keySet()){
				JFLAPDebug.print(machine1+" "+tm+" "+machine1.equals(tm));
				JFLAPDebug.print(graphMap.get(machine1));
				JFLAPDebug.print(graphMap.containsKey(tm));
			}
			blockGraph.setGraph(b, graphMap.get(tm));
		}
		
		for (Transition<?> t : machine.getTransitions()) {
			State from = t.getFromState(), to = t.getToState();
			Point2D edge = graph.getControlPt(from, to);
			blockGraph.setControlPt(edge, from, to);
		}
		return blockGraph;
	}
	
	@Override
	public Element appendComponentsToRoot(Document doc,
			TransitionGraph<? extends Transition<?>> graph, Element root) {
		BlockTMGraph blockGraph = (BlockTMGraph) graph;
		BlockTuringMachine auto = blockGraph.getAutomaton();
		
		root = super.appendComponentsToRoot(doc, graph, root);
		Element sub_elem = XMLHelper.createElement(doc, SUBGRAPH_TAG, null, null);
		
		for(State s : auto.getStates()){
			Block b = (Block) s;
			TransitionGraph bGraph = blockGraph.getGraph(b);
			
			XMLTransducer<TransitionGraph> trans = TransducerFactory.getTransducerForStructure(bGraph);
			sub_elem.appendChild(trans.toXMLTree(doc, bGraph));
		}
		root.appendChild(sub_elem);
		
		return root;
	}
}
