package view.action.grammar.parse;

import model.algorithms.testinput.parse.ParserException;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.grammar.Grammar;
import model.grammar.typetest.matchers.CNFChecker;

import view.grammar.GrammarView;
import view.grammar.parsing.ParserView;
import view.grammar.parsing.cyk.CYKParseView;

public class CYKParseAction extends ParseAction<CYKParser>{
	
	public CYKParseAction(GrammarView view){
		super("CYK Parse", view);
	}

	@Override
	public ParserView<CYKParser> createParseView(Grammar g) {
		if(!new CNFChecker().matchesGrammar(g)) throw new ParserException("The grammar must be in CNF form to be parsed!");
		
		return new CYKParseView(new CYKParser(g));
	}

}
