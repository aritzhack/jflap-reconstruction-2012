package model.algorithms.testinput.parse.cyk;

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

import debug.JFLAPDebug;

import model.algorithms.testinput.parse.*;
import model.change.events.AdvancedChangeEvent;
import model.grammar.*;
import model.grammar.typetest.GrammarType;
import model.symbols.*;

public class CYKParser extends Parser {

	private List<Production> myAnswerTrace;
	private Set<CYKParseNode> myNodeTable[][];
	private Set<Symbol> mySetTable[][];
	private int myIncrement;
	public static final int CELL_CHANGED = 4;

	/**
	 * Constructor for the CYKParser
	 * 
	 * @param g
	 *            - grammar must be in Chomsky Normal Form (CNF)
	 */
	public CYKParser(Grammar g) {
		super(g);
	}

	/**
	 * Sets <CODE>myParseTable</CODE> to a new Set[][] of size
	 * length*(length+1)/2
	 * 
	 * @param length
	 *            the size of the string being processed by the table.
	 */
	private void initializeTable(int length) {
		myNodeTable = new Set[length][length];
		mySetTable = new Set[length][length];

		for (int i = 0; i < length; i++) {
			for (int j = i; j < length; j++) {
				myNodeTable[i][j] = new HashSet<CYKParseNode>();
				mySetTable[i][j] = new HashSet<Symbol>();
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
	private boolean addTerminalProductions() {
		ProductionSet productions = getGrammar().getProductionSet();
		
		for (int i = 0; i < getInput().size(); i++) {
			SymbolString current = getInput().subList(i, i + 1);

			for (Production p : productions) {
				if (p.equalsRHS(current)) {
					CYKParseNode node = new CYKParseNode(p, i);
					myNodeTable[i][i].add(node);
				}
			}
			if (myNodeTable[i][i].size() == 0)
				throw new ParserException("There aren't valid terminal productions!");
		}
		return true;
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
	private boolean addNonterminalProductions() {
		int size = getInput().size();
		if (myIncrement >= size)
			return false;

		for (int i = 0; i < size; i++) {
			for (int j = i + myIncrement; j < size; j++) {
				//already filled out this cell
				if(myNodeTable[i][j].size() > 0) continue;
				
				findAllProductions(i, j);
			}
		}
		return true;
	}

	private void findAllProductions(int i, int j) {
		for (int k = i; k < j; k++) {
			for (Symbol A : getLHSVariablesForNode(i, k)) {
				for (Symbol B : getLHSVariablesForNode(k + 1, j)) {
					SymbolString concat = new SymbolString(A, B);

					for (Production p : getGrammar().getProductionSet()) {
						if (p.equalsRHS(concat)) {
							CYKParseNode node = new CYKParseNode(p, k);

							myNodeTable[i][j].add(node);
						}
					}
				}
			}
		}
	}

	/**
	 * Returns a LeftmostDerivation that can derive the specified string. For
	 * example, if the derivation of string aabaa is: S -> BA -> aA -> aAA ->
	 * aBCA -> aaCA -> aabA -> aabBC -> aabaC -> aabaa, then the Derivation
	 * returned would be: [S->B A, B->a, A->A A, A->B C, B->a, C->b, A->B C,
	 * B->a, C->b]
	 * 
	 */
	public Derivation getDerivation() {
		myAnswerTrace = new ArrayList<Production>();
		Variable start = getGrammar().getStartVariable();

		getTrace(start, 0, getInput().size() - 1);

		Derivation answer = Derivation.createLeftmostDerivation(myAnswerTrace);
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
	private boolean getTrace(Variable LHS, int start, int end) {
		ProductionSet productions = getGrammar().getProductionSet();
		if (start == end) {
			Terminal character = (Terminal) getInput().get(start);
			Production terminalProduction = new Production(LHS, character);

			for (Production p : productions) {
				if (p.equals(terminalProduction)) {
					myAnswerTrace.add(terminalProduction);
					return true;
				}
			}
			return false;
		}
		for (CYKParseNode node : myNodeTable[start][end]) {
			Production nodeProduction = new Production(LHS, node.getRHS());

			for (Production p : productions) {
				if (p.equals(nodeProduction)) {
					myAnswerTrace.add(nodeProduction);
					if (getTrace(node.getFirstRHSVariable(), start, node.getK())
							&& getTrace(node.getSecondRHSVariable(),
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


	@Override
	public boolean isAccept() {
		//TODO
		return mySetTable[0][getInput().size()-1].contains(getGrammar().getStartVariable());
	}

	@Override
	public boolean isDone() {
		
		return getInput()!=null && myIncrement > getInput().size();
	}

	public boolean calculateNextRow() {
		boolean row;
		if (myIncrement == 0)
			row = addTerminalProductions();
		else
			row = addNonterminalProductions();
		myIncrement++;
		return row;
	}

	@Override
	public boolean resetInternalStateOnly() {

		myAnswerTrace = new ArrayList<Production>();
		myIncrement = 0;

		if (getInput() != null) {
			initializeTable(getInput().size());
			return calculateNextRow();
		}
		
		return true;
	}

	@Override
	public boolean setInput(SymbolString string) {
		if (string != null && string.size() == 0) {
			throw new ParserException(
					"CNF Grammars cannot produce empty strings!");
		}
		return super.setInput(string);
	}

	private Set<Symbol> getLHSVariablesForNode(int row, int col) {
		if (myNodeTable[row][col] != null) {
			Set<Symbol> set = new TreeSet<Symbol>();

			for (CYKParseNode node : myNodeTable[row][col]) {
				set.add(node.getLHS());
			}
			return set;
		}
		return null;
	}

	@Override
	public boolean stepParser() {
		int previousIncrement = myIncrement-1;
		for (int i=0; i +previousIncrement < getInput().size(); i++) {
			Set<Symbol> enteredSet = getLHSVariablesForNode(i, i+previousIncrement);
			validate(i, i+previousIncrement, enteredSet);
		}
		calculateNextRow();
		return true;
	}
	
	public void doSelected(int row, int col){
		if(!isCellEditable(row, col)) return;
		Set<Symbol> selectedSet = getLHSVariablesForNode(row, col);
		insertSet(row, col, selectedSet);
	}

	public boolean validate(int row, int col, Set<Symbol> set) {
		Set<Symbol> valid = getLHSVariablesForNode(row, col);
		if(set.equals(valid)){
			mySetTable[row][col] = set;
			distributeChange(new AdvancedChangeEvent(this, CELL_CHANGED , mySetTable[row][col]));
		}
		return set.equals(valid);
	}
	
	public boolean insertSet(int row, int col, Set<Symbol> set){
		boolean valid = validate(row, col, set);
		if(valid && rowIsComplete()) calculateNextRow();
		return valid;
	}

	private boolean rowIsComplete() {
		int previousIncrement = myIncrement - 1;
		for(int i=0; i+previousIncrement < getInput().size(); i++){
			if(!getLHSVariablesForNode(i, i+previousIncrement).equals(mySetTable[i][i+previousIncrement]))
				return false;
		}
		return true;
	}
	
	public Set<Symbol> getValueAt(int row, int col){
		if(col-row >= myIncrement) return null;
		return mySetTable[row][col];
	}

	public boolean isCellEditable(int row, int col) {
		return col - row == myIncrement-1 && !getLHSVariablesForNode(row, col).equals(mySetTable[row][col]);
	}
}
