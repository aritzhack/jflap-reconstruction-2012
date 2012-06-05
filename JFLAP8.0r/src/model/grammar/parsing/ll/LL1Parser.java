package model.grammar.parsing.ll;

import model.algorithms.AlgorithmStep;
import model.grammar.Grammar;
import model.grammar.parsing.Derivation;
import model.grammar.parsing.Parser;
import model.grammar.parsing.ParserException;
import model.grammar.typetest.GrammarType;


public class LL1Parser extends Parser {

	private LL1ParseTable myParseTable;

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
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean stepParser() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Derivation retrieveDerivation() {
		// TODO Auto-generated method stub
		return null;
	}

	
	private class CheckMatchStep implements AlgorithmStep {
		
	}
}
