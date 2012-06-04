package model.grammar.parsing.cyk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.formaldef.components.symbols.Variable;

public class CYKTracer {
	private Map<Integer, Map<Integer, Set<CYKTracerNode>>> myNodeMap;
	
	public CYKTracer(int size){
		myNodeMap = new HashMap<Integer, Map<Integer, Set<CYKTracerNode>>>();
		for(int i=0; i<size;i++){
			Map innerMap = new HashMap<Integer, Set<CYKTracerNode>>();
			for (int j = i; j < size; j++) {
				HashSet<CYKTracerNode> innerSet = new HashSet<CYKTracerNode>();
				innerMap.put(j, innerSet);
			}
			myNodeMap.put(i, innerMap);
		}
	}
	
	public Set<CYKTracerNode> getNodeSet(int row, int col){
		return myNodeMap.get(row).get(col);
	}
	
	public void addNode(int row, int col, CYKTracerNode node){
		myNodeMap.get(row).get(col).add(node);
	}
}
