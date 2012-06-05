package test;

import model.automata.InputAlphabet;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;
import model.grammar.parsing.FirstFollowTable;
import model.regex.OperatorAlphabet;
import model.regex.RegularExpressionGrammar;

public class ParserTest extends GrammarTest {

	@Override
	public void runTest() {
		
		TerminalAlphabet terms = new TerminalAlphabet();
		VariableAlphabet vars = new VariableAlphabet();
		ProductionSet prod = new ProductionSet();
		StartVariable var = new StartVariable();
		Grammar g  = new Grammar(vars,
									terms,
									prod, 
									var);
		
		Variable S = new Variable("S");
		Variable A = new Variable("A");
		Variable B = new Variable("B");
		Terminal a = new Terminal("a");
		Terminal b = new Terminal("b");

		//ex7.6cnf-a.jff
		addSymbols(g.getVariables(), S,A,B );
		addSymbols(g.getTerminals(),a,b);
		prod.add(new Production(S, a,S,b));
		prod.add(new Production(S, a,A,b));
		prod.add(new Production(A, a,A));
		prod.add(new Production(A, B));
		prod.add(new Production(B, b));
		prod.add(new Production(B));
		g.setStartVariable(S);
		
		outPrintln(g.toString());
		
		//construct First/Follow table
		FirstFollowTable table = new FirstFollowTable(g);
		outPrintln(table.toString());
		
		//construct First/Follow table
		table = new FirstFollowTable(new RegularExpressionGrammar(new InputAlphabet(), new OperatorAlphabet()));
		outPrintln(table.toString());
	}

	@Override
	public String getTestName() {
		return null;
	}

}
