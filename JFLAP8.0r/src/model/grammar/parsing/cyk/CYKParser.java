package model.grammar.parsing.cyk;

/**
 * Implementation of the CYK (Cocke-Younger-Kasami) parsing algorithm
 * to determine whether a string is in the language of a given grammar 
 * and the trace (set of productions) followed to derive the string
 * 
 * Note: CYKParser was redone in Summer 2012 with improved efficiency
 * and significant code redesign/refactoring. The grammar passed in
 * must be in Chomsky Normal Form (CNF) for the parser to work correctly.
 * 
 * @author Peggy Li
 * @author Ian McMahon
 */

import java.util.*;

import model.algorithms.*;
import model.formaldef.components.symbols.*;
import model.grammar.*;
import model.grammar.parsing.*;
import model.grammar.typetest.GrammarType;

public class CYKParser extends Parser {

	private ProductionSet myProductions;
	private List<Production> myAnswerTrace;
	private Variable myStartVariable;
	private Set<CYKParseNode> myParseTable[][];
	private int currentStart, currentIncrement;

	/**
	 * Constructor for the CYKParser
	 * 
	 * @param g
	 *            - grammar must be in Chomsky Normal Form (CNF)
	 */
	public CYKParser(Grammar g) {
		super(g);
		this.resetParserStateOnly();
	}

	/**
	 * Sets <CODE>myParseTable</CODE> to a new Set[][] of size
	 * length*(length+1)/2
	 * 
	 * @param length
	 *            the size of the string being processed by the table.
	 */
	private void initializeTable(int length) {
		myParseTable = new Set[length][length];
		for (int i = 0; i < length; i++) {
			for (int j = i; j < length; j++) {
				myParseTable[i][j] = new HashSet<CYKParseNode>();
			}
		}
	}

	/**
	 * Adds the terminal productions for each terminal in the input string to
	 * the CYK parse table For the terminal <code>r</code> at index
	 * <code>i</code> of the input, for each production L -> r where L is the
	 * LHS variable deriving r, a node representing L is added to row i, column
	 * i in the parse table
	 * 
	 * For example, for the input string <code>aabb</code> for the grammar S ->
	 * AB | CB C -> AS A -> a B -> b for the language a^n b^n | n > 0 the parse
	 * table would hold an A node at [0, 0] and [1, 1] and a B node at [2, 2]
	 * and [3, 3]
	 * 
	 * @param input
	 *            - the entire string to be parsed
	 * @param length
	 *            - the length of the input string
	 */
	private boolean addTerminalProduction() {
		int i = currentStart;
		SymbolString current = getCurrentInput().subList(i, i + 1);
		for (Production p : myProductions) {
			if (p.getRHS().equals(current)) {
				CYKParseNode node = new CYKParseNode(p, i);
				myParseTable[i][i].add(node);
			}
		}
		return myParseTable[i][i].size()>0;
	}

	/**
	 * Find and add to the parse table the LHS of all productions in the grammar
	 * if the production's RHS can derive the substring from index
	 * <code>start</code> to index <code>end</end> in the input string,
	 * excluding the terminal productions (substring of length 1)
	 * 
	 * Tests all possible concatenations of substrings for all possible increments k
	 * e.g. 'abc' [0, 2] can be formed by concatenating 
	 * 		'a' [0, 1] with 'bc' [1, 2] with k = 1
	 * 		or 'ab' [0, 2] with 'c' [2, 2] with k = 2
	 * 
	 * @param start
	 *            - start index of the substring in the input
	 * @param end
	 *            - end index of the substring in the input
	 */
	private boolean findProductions() {
		for (int k = currentStart; k < currentStart+currentIncrement; k++) {
			for (Variable A : getLHSVariableSet(currentStart, k)) {
				for (Variable B : getLHSVariableSet(k + 1, currentStart+currentIncrement)) {
					SymbolString concat = new SymbolString(A, B);
					for (Production p : myProductions) {
						if (p.getRHS().equals(concat)) {
							CYKParseNode node = new CYKParseNode(p, k);
							myParseTable[currentStart][currentStart+currentIncrement].add(node);
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Returns a list of Productions that (in order of leftmost derivation) can
	 * derive the specified string. For example, if the derivation of string
	 * aabaa is: S -> BA -> aA -> aAA -> aBCA -> aaCA -> aabA -> aabBC -> aabaC
	 * -> aabaa, then the list returned would be: [S->B A, B->a, A->A A, A->B C,
	 * B->a, C->b, A->B C, B->a, C->b]
	 * 
	 */
	public Derivation retrieveDerivation() {
		myAnswerTrace = new ArrayList<Production>();
		getPossibleTrace(getGrammar().getStartVariable(), 0,
				getCurrentInput().size() - 1);
		
		Derivation answer = new Derivation(new Integer[0],myAnswerTrace.toArray(new Production[0]));
		return answer;
	}

	/**
	 * Recursive backtracking helper function that modifies
	 * <CODE>myAnswerTrace</CODE> and will return true if and only if it finds a
	 * possible derivation of the string specified by the LHS variable and start
	 * and end indexes.
	 * 
	 * @param LHS
	 *            the variable to be checked for possibly being able to derive
	 *            the string.
	 * @param start
	 *            the index of first symbol in the string.
	 * @param end
	 *            the index of final symbol in the string.
	 */
	private boolean getPossibleTrace(Variable LHS, int start, int end) {
		if (start == end) {
			Production terminalProduction = new Production(LHS,
					(Terminal) getCurrentInput().get(start));
			for (Production p : myProductions) {
				if (p.equals(terminalProduction)) {
					myAnswerTrace.add(terminalProduction);
					return true;
				}
			}
			return false;
		}
		for (CYKParseNode node : myParseTable[start][end]) {
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
		return "This is a CYK Parser!";
	}

	/**
	 * Return CNF enum from GrammarType class; CYK parser requires that the
	 * grammar be in Chomsky Normal Form
	 */
	public GrammarType getRequiredGrammarType() throws ParserException {
		return GrammarType.CHOMSKY_NORMAL_FORM;
	}

	/**
	 * Returns all variables that can derive the symbols specified by start and
	 * end
	 * 
	 * @param start
	 *            the index of the first symbol.
	 * @param end
	 *            the index of the final symbol.
	 */
	private Set<Variable> getLHSVariableSet(int start, int end) {
		Set<Variable> LHSVars = new HashSet<Variable>();
		for (CYKParseNode node : myParseTable[start][end]) {
			LHSVars.add(node.getLHS());
		}
		return LHSVars;
	}

	@Override
	public boolean isAccept() {
		return getLHSVariableSet(0, getCurrentInput().size() - 1).contains(
				myStartVariable);
	}

	@Override
	public boolean isDone() {
		return currentIncrement >= getCurrentInput().size();
	}

	@Override
	public boolean stepParser() {
		boolean step;
		if (currentIncrement == 0) {
			step = addTerminalProduction();
		}else{
			step = findProductions();
		}
		currentStart++;
		if(currentIncrement+currentStart >= getCurrentInput().size()){
			currentStart = 0;
			currentIncrement++;
		}
		return step;
		
	}

	@Override
	public boolean resetParserStateOnly() {
		myProductions = getGrammar().getProductionSet();
		myStartVariable = getGrammar().getStartVariable();
		myAnswerTrace = new ArrayList<Production>();
		currentStart = currentIncrement = 0;
		if (getCurrentInput() != null) {
			this.initializeTable(getCurrentInput().size());
		}
		return true;
	}

	@Override
	public boolean setInput(SymbolString string) {
		if (string!= null && string.size() == 0) {
			throw new ParserException(
					"CNF Grammars cannot produce empty strings!");
		}
		return super.setInput(string);
	}

	public List<Production> getTrace() {
		retrieveDerivation();
		return myAnswerTrace;
	}

}
