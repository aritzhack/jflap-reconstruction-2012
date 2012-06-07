package model.grammar.parsing.brute.bad;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

import jflap.debug.JFLAPDebug;
import jflap.errors.BooleanWrapper;
import jflap.model.formaldef.symbols.Symbol;
import jflap.model.formaldef.symbols.SymbolString;
import jflap.model.formaldef.symbols.terminal.Terminal;
import jflap.model.grammar.Grammar;
import jflap.model.grammar.Production;
import jflap.model.grammar.parse.ParseNode;
import jflap.model.grammar.parse.Parser;


public abstract class BaseParser extends Parser {

	/** Stuff for the possibilities. **/
	private static final Production[] P = new Production[0];
	private static final int[] S = new int[0];

	/** Starting ParseNode **/
	protected static final ParseNode E = new ParseNode(new SymbolString(), P, S);

	/** The array of productions. */
	protected Production[] myProductions;

	/** This is the target string. */
	protected SymbolString myTarget;

	/** This should be set to done when the operation has completed. */
	protected boolean isDone = false;

	/** The "answer" to the parse question. */
	protected ParseNode myAnswer;

	/**
	 * The "smaller" set, those symbols that may possibly reduce to nothing.
	 */
	protected Set<Symbol> mySmallerSet;

	/** The Production rule that the User has set to apply to the String **/
	protected Production myCurrentProduction;

	/** Integer variable that shows how many times the derivation has occured **/
	protected int myCount=0;

	/** This holds the list of nodes for the BFS. */
	protected LinkedList<ParseNode> myQueue = new LinkedList<ParseNode>();


	public BaseParser(Grammar g) {
		super(g);
	}


	/**
	 * This will initialize data structures.
	 */
	@Override
	public BooleanWrapper init(SymbolString target) {
		for (Symbol s: target)
			if (!Grammar.isTerminal(s))
				return new BooleanWrapper(false, 
						"String to parse has nonterminal " + s + ".");

		Grammar grammar = getGrammar();

		if (grammar == null)
			return new BooleanWrapper(false, "This grammar accepts no language.");

		myQueue.clear();

		myAnswer=new ParseNode(new SymbolString(grammar.getStartVariable()), P, S);
		myQueue.add(myAnswer);
		mySmallerSet = Collections.unmodifiableSet(Unrestricted
				.smallerSymbols(grammar));
		myProductions = grammar.getProductions();
		myTarget = target;
		return new BooleanWrapper(true);
	}

	/**
	 * This method retrieves the previous step performed by the user
	 * @return
	 */
	public ParseNode getPreviousAnswer()
	{
		myAnswer=(ParseNode) myAnswer.getParent();
		myQueue.clear();
		myQueue.add(myAnswer);
		return myAnswer;
	}

	public abstract boolean start();

	/**
	 * Returns if the parser has finished, with success or otherwise.
	 * 
	 * @return <CODE>true</CODE> if the
	 */
	public synchronized boolean isFinished() {
		return isDone;
	}

	/**
	 * This returns the answer node for the parser.
	 * 
	 * @return the answer node for the parse, or <CODE>null</CODE> if there
	 *         was no answer, or one has not been discovered yet
	 */
	public synchronized ParseNode getAnswer() {
		return myAnswer;
	}


}
