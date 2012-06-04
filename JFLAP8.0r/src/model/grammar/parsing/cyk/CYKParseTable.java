package model.grammar.parsing.cyk;

import java.util.*;

import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;

public class CYKParseTable {

	private Map<Integer, Map<Integer, Set<Variable>>> myParseTable;
	
	public CYKParseTable (int size) {
		myParseTable = new HashMap<Integer, Map<Integer, Set<Variable>>>();
		for (int i = 0; i < size; i++) {
			HashMap<Integer, Set<Variable>> innerMap = new HashMap<Integer, Set<Variable>>();
			for (int j = i; j < size; j++) {
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
	 * Add a single variable to the specified location of the map
	 * 
	 * For example, addVariable(2, 3, A) would add the variable A to 
	 * the third (0-based index for row 2) key of the parse table, and
	 * then at the fourth (0-based index for col 3) key of the inner map
	 * which is the value for row 2 of the outer map
	 * in this way, the map can be considered like a 2-D array 
	 */
	public void addVariable(int row, int col, Variable v) {
		myParseTable.get(row).get(col).add(v);
	}
	
	
	
	
}
