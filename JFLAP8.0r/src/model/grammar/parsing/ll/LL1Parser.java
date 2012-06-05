package model.grammar.parsing.ll;

import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.parsing.Derivation;
import model.grammar.parsing.Parser;
import model.grammar.parsing.ParserException;
import model.grammar.typetest.GrammarType;


public class LL1Parser extends Parser {

	private LL1ParseTable myParseTable;
	private Derivation myDerivation;
	private SymbolString myStack;
	private SymbolString myUnprocessedInput;

	public LL1Parser(Grammar g) {
		this(g, new LL1ParseTable(g));
	}

	public LL1Parser(Grammar g, LL1ParseTable table) {
		super(g);
		myParseTable = table;
	}

	@Override
	public String getDescriptionName() {
		return "LL1 parser";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public boolean resetParserStateOnly() {
		myUnprocessedInput = this.getInput();
		myDerivation = new Derivation(createEmptyStart());
		return false;
	}

	private Production createEmptyStart() {
		return new Production(new SymbolString(),
							new SymbolString(getGrammar().getStartVariable()));
	}

	@Override
	public boolean isAccept() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GrammarType getRequiredGrammarType() throws ParserException {
		return GrammarType.LL1;
	}

	@Override
	public boolean stepParser() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Derivation getDerivation() {
		// TODO Auto-generated method stub
		return null;
	}

	
	private class CheckMatchStep implements AlgorithmStep {

		@Override
		public String getDescriptionName() {
			return null;
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isComplete() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
