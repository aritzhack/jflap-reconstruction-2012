package model.grammar.parsing.cyk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import model.formaldef.components.symbols.Variable;

/**
 * The <CODE>CYKParseTable</CODE> functions as a map with 2 Integers as keys and
 * sets of <CODE>CYKParseNode</CODE> as values. Used to track possible
 * derivations of strings within the CYK Parsing Algorithm.
 * 
 * @author Ian McMahon, Peggy Li
 * 
 * 
 */
public class CYKParseTable {
	private Map<Integer, Map<Integer, Set<CYKParseNode>>> myNodeMap;

	/**
	 * This will instantiate a new <CODE>CYKParseTable</CODE> of size length(length+1)/2.
	 * @param length
	 * 			the length of the string the parse table will determine possible derivations of.
	 */
	public CYKParseTable(int length) {
		myNodeMap = new HashMap<Integer, Map<Integer, Set<CYKParseNode>>>();
		for (int i = 0; i < length; i++) {
			Map<Integer, Set<CYKParseNode>> innerMap = new HashMap<Integer, Set<CYKParseNode>>();
			for (int j = i; j < length; j++) {
				HashSet<CYKParseNode> innerSet = new HashSet<CYKParseNode>();
				innerMap.put(j, innerSet);
			}
			myNodeMap.put(i, innerMap);
		}
	}

	/**
	 * This will return a set of all CYKParseNodes associated with the specific
	 * start and end indexes.
	 * 
	 * @param start
	 *            the index of the first symbol in the string
	 * @param end
	 *            the index of the final symbol in the string
	 */
	public Set<CYKParseNode> getNodeSet(int start, int end) {
		return myNodeMap.get(start).get(end);
	}

	/**
	 * This will return a set of all variables that can derive the string
	 * specified by start and end within the associated grammar's production
	 * rules.
	 * 
	 * @param start
	 *            the index of the first symbol in the string
	 * @param end
	 *            the index of the final symbol in the string
	 */
	public Set<Variable> getLHSVariableSet(int start, int end) {
		Set<Variable> LHSVariables = new HashSet<Variable>();
		for (CYKParseNode node : myNodeMap.get(start).get(end)) {
			LHSVariables.add(node.getLHS());
		}
		return LHSVariables;
	}

	/**
	 * Adds a single <CODE>CYKParseNode</CODE> to the <CODE>CYKParseTable</CODE>
	 * at the index specified by start and end
	 * 
	 * @param start
	 *            the index of the first symbol in the string
	 * @param end
	 *            the index of the final symbol in the string
	 * @param node
	 *            <CODE>CYKParseNode</CODE> specifying a possible derivation of
	 *            the string
	 */
	public void addNode(int start, int end, CYKParseNode node) {
		myNodeMap.get(start).get(end).add(node);
	}
}
