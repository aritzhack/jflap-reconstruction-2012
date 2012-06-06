package model.grammar.parsing.brute;

import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;

public class BruteParserTester {
	
	public static void main (String[] args) {
		
		Variable S = new Variable("S"), A = new Variable("A"), B = new Variable("B");
		Terminal a = new Terminal("a"), b = new Terminal("b"), c = new Terminal("c");
		
		VariableAlphabet v = new VariableAlphabet();
		v.addAll(S, A, B);
		
		TerminalAlphabet t = new TerminalAlphabet();
		t.addAll(a, b, c);
		
		
		ProductionSet p = new ProductionSet();
//		p.add(new Production(S, A, B));
//		p.add(new Production(A, a));
//		p.add(new Production(A, A, a));
//		p.add(new Production(B, b, b));
//		p.add(new Production(B, c));
		p.add(new Production(S, A, B));
		p.add(new Production(A, a));
		p.add(new Production(B, b));
		
		StartVariable s = new StartVariable("S");
		
		Grammar g = new Grammar(v, t, p, s);
		
		RestrictedBruteParser parser = new RestrictedBruteParser(g);
		
		parser.setInput(new SymbolString(a, b));
		
		//parser.stepParser();
		//System.out.println(parser.isAccept());
		
		//parser.resetParserStateOnly();
		parser.stepParser();
		parser.stepParser();
		parser.stepParser();
		System.out.println(parser.isAccept());
	}

}
