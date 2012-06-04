package model.grammar.parsing.cyk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.formaldef.components.symbols.Variable;

public class CYKParseTable {
	private Map<Integer, Map<Integer, Set<CYKParseNode>>> myNodeMap;
	
	public CYKParseTable(int size){
		myNodeMap = new HashMap<Integer, Map<Integer, Set<CYKParseNode>>>();
		for(int i=0; i<size;i++){
			Map<Integer, Set<CYKParseNode>> innerMap = new HashMap<Integer, Set<CYKParseNode>>();
			for (int j = i; j < size; j++) {
				HashSet<CYKParseNode> innerSet = new HashSet<CYKParseNode>();
				innerMap.put(j, innerSet);
			}
			myNodeMap.put(i, innerMap);
		}
	}
	
	public Set<CYKParseNode> getNodeSet(int row, int col){
		return myNodeMap.get(row).get(col);
	}
	
	public Set<Variable> getLHSVariableSet(int row, int col){
		Set<Variable> LHSVariables = new HashSet<Variable>();
		for(CYKParseNode node : myNodeMap.get(row).get(col)){
			LHSVariables.add(node.getLHS());
		}
		return LHSVariables;
	}
	
	public void addNode(int row, int col, CYKParseNode node){
		myNodeMap.get(row).get(col).add(node);
	}
}
