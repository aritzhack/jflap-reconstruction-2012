package model.grammar.parsing.cyk;

/**
 * CYK parser redesign, Summer 2012
 * @author Peggy Li, Ian McMahon
 */

import java.util.*;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.parsing.Parser;

public class CYKParser extends Parser {

	private ProductionSet myProductions;
	private List<Production> myAnswerTrace;
	private Variable myStartVariable;
	private SymbolString myTarget;

	private CYKParseTable myTracer;

	public CYKParser(Grammar g) {
		super(g);

		myProductions = g.getProductionSet();
		myStartVariable = g.getStartVariable();
	}


	public boolean parse(SymbolString input) {
		myTarget = input;
		int length = input.size();
		myTracer = new CYKParseTable(length);

		addTerminalProductions(input, length);

		for (int increment = 1; increment < length; increment++) {
			for (int row = 0; row < length - 1; row++) {
				int substringLength = row + increment;
				if (length <= substringLength) {
					break;
				}
				findProductions(row, substringLength);
			}
		}
		return myTracer.getLHSVariableSet(0, length - 1).contains(
				myStartVariable);
	}


	/**
	 * Adds the terminal productions for each terminal in the input string
	 * to the CYK parse table
	 * For the terminal <code>r</code> at index <code>i</code> of the input, 
	 * for each production L -> r where L is the LHS variable deriving r,
	 * a node representing L is added to row i, column i in the parse table
	 * 
	 * For example, for the input string <code>aabb</code> for the grammar
	 * S -> AB | CB
	 * C -> AS
	 * A -> a
	 * B -> b
	 * for the language a^n b^n | n > 0
	 * the parse table would hold an A node at [0, 0] and [1, 1]
	 * and a B node at [2, 2] and [3, 3]
	 * 
	 * @param input - the entire string to be parsed
	 * @param length - the length of the input string
	 */
	private void addTerminalProductions(SymbolString input, int length) {
		for (int i = 0; i < length; i++) {
			SymbolString current = input.subList(i, i + 1);
			for (Production p : myProductions) {
				if (p.getRHS().equals(current)) {
					CYKParseNode node = new CYKParseNode(p, i);
					myTracer.addNode(i, i, node);
				}
			}
		}
	}

	// finds all productions whose RHS matches the substring
	private void findProductions(int start, int end) {
		for (int k = start; k < end; k++) {
			for (Variable A : myTracer.getLHSVariableSet(start, k)) {
				for (Variable B : myTracer.getLHSVariableSet(k + 1, end)) {
					SymbolString concat = new SymbolString(A, B);
					for (Production p : myProductions) {
						if (p.getRHS().equals(concat)) {
							CYKParseNode node = new CYKParseNode(p, k);
							myTracer.addNode(start, end, node);
						}
					}
				}
			}
		}
	}

	public List<Production> getTrace() {
		myAnswerTrace = new ArrayList<Production>();
		getPossibleTrace(getGrammar().getStartVariable(), 0,
				myTarget.size() - 1);
		return myAnswerTrace;
	}

	private boolean getPossibleTrace(Variable LHS, int start, int end) {
		if (start == end) {
			Production terminalProduction = new Production(LHS,
					(Terminal) myTarget.get(start));
			for (Production p : myProductions) {
				if (p.equals(terminalProduction)) {
					myAnswerTrace.add(terminalProduction);
					return true;
				}
			}
			return false;
		}
		for (CYKParseNode node : myTracer.getNodeSet(start, end)) {
			Production nodeProduction = new Production(LHS, node.getRHS());
			for (Production p : myProductions) {
				if (p.equals(nodeProduction)) {
					myAnswerTrace.add(nodeProduction);
					if (getPossibleTrace(node.getFirstRHSVariable(), start,
							node.getK())
							&& getPossibleTrace(node.getSecondRHSVariable(),
									node.getK() + 1, end)) {
						return true;
					}
					myAnswerTrace.remove(nodeProduction);
					return false;
				}
			}
		}
		return false;
	}


	@Override
	public String getDescriptionName() {
		return "CYK Parser";
	}


	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object copy() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
