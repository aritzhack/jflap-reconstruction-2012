package test;

import debug.JFLAPDebug;
import model.automata.InputAlphabet;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;
import model.grammar.parsing.Derivation;
import model.grammar.parsing.FirstFollowTable;
import model.grammar.parsing.ll.LL1Parser;
import model.grammar.parsing.lr.SLR1DFA;
import model.grammar.parsing.lr.SLR1ParseTable;
import model.grammar.parsing.lr.SLR1Parser;
import model.regex.OperatorAlphabet;
import model.regex.RegularExpressionGrammar;
import model.util.UtilFunctions;

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
		Terminal c = new Terminal("c");
		Terminal d = new Terminal("d");
		
		//ex7.6cnf-a.jff
		addSymbols(g.getVariables(), S,A,B );
		addSymbols(g.getTerminals(),a,b,c,d);
		prod.add(new Production(S, a,A,d,B));
		prod.add(new Production(A, a,A));
		prod.add(new Production(A, c));
		prod.add(new Production(B, b, B));
		prod.add(new Production(B));
		g.setStartVariable(S);
		
		outPrintln(g.toString());
		
		//construct First/Follow table
		FirstFollowTable table = new FirstFollowTable(g);
		outPrintln(table.toString());
		
		//construct First/Follow table
		table = new FirstFollowTable(new RegularExpressionGrammar(new InputAlphabet(), new OperatorAlphabet()));
		outPrintln(table.toString());
		
		//try LL1 parser
		String in = "$";
		LL1Parser ll1parse = new LL1Parser(g);
		boolean accepts = ll1parse.quickParse(SymbolString.createFromString(in, g));
		outPrintln("LL1 Accept? " + accepts + "\n" + createPrintout(ll1parse.getDerivation()));
		JFLAPDebug.print(ll1parse.getStack());
		//prepare and execute SLR parse
		prod.clear();
		prod.add(new Production(S, A));
		prod.add(new Production(A,a,a,A));
		prod.add(new Production(A, b));
		g.trimAlphabets();
		outPrintln("SLR1 Grammar: " + g);
		
		//Show FirstFollow
		table = new FirstFollowTable(g);
		outPrintln(table.toString());
		
		//Show DFA
		SLR1DFA dfa = new SLR1DFA(g);
		outPrintln(dfa.toString());
		
		//Show SLR1 parse Table
		SLR1ParseTable slrTable = new SLR1ParseTable(g);
		outPrintln(slrTable.toString());

		in = "aaaab";
		SLR1Parser slr1parse = new SLR1Parser(g);
		accepts = slr1parse.quickParse(SymbolString.createFromString(in, g));
		outPrintln("SLR1 Accept? " + accepts + "\n" + createPrintout(slr1parse.getDerivation()));
		
	}

	private String createPrintout(Derivation derivation) {
		SymbolString[] s = derivation.getResultArray();
		String str = "";
		for (int i = 0; i< s.length; i++){
			str += derivation.getProduction(i) + "\t" + s[i] + "\n";
		}

		return str;
	}

	@Override
	public String getTestName() {
		return null;
	}

}
