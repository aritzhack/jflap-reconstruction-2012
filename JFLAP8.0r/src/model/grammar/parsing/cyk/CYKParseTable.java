package model.grammar.parsing.cyk;

import java.util.*;

import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;

public class CYKParseTable {

	private Map<Integer, Map<Integer, Set<Variable>>> myParseTable;
	
	public CYKParseTable (SymbolString input) {
		myParseTable = new HashMap<Integer, Map<Integer, Set<Variable>>>();
		for (int i = 0; i < input.size(); i++) {
			HashMap<Integer, Set<Variable>> innerMap = new HashMap<Integer, Set<Variable>>();
			for (int j = i; j < input.size(); j++) {
				HashSet<Variable> innerSet = new HashSet<Variable>();
				innerMap.put(j, innerSet);
			}
			myParseTable.put(i, innerMap);
		}
	}

	public Set<Variable> getVariableSet(int row, int col) {
		return myParseTable.get(row).get(col);
		
	}
	
	/*
	 * Warning: replaces ALL existing items in set with parameter vars contents
	 */
	public void putVariableSet(int row, int col, Set<Variable> vars) {
		myParseTable.get(row).put(col, vars);
	}
	
	/*
	 * Add individual variable to existing set
	 */
	public void addVariable(int row, int col, Variable v) {
		myParseTable.get(row).get(col).add(v);
	}
	
	
	
	
}
