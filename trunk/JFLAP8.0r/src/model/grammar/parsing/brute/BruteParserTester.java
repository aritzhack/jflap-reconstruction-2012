package model.grammar.parsing.brute;

import model.automata.InputAlphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;
import model.regex.OperatorAlphabet;
import model.regex.RegularExpressionGrammar;

public class BruteParserTester {
	
	public static void main (String[] args) {
		
//		Variable S = new Variable("S"), A = new Variable("A"), B = new Variable("B");
//		Terminal a = new Terminal("a"), b = new Terminal("b"), c = new Terminal("c");
//		
//		VariableAlphabet v = new VariableAlphabet();
//		v.addAll(S, A, B);
//		
//		TerminalAlphabet t = new TerminalAlphabet();
//		t.addAll(a, b, c);
//		
//		
//		ProductionSet p = new ProductionSet();
//		p.add(new Production(S, A, B));
//		p.add(new Production(A, a));
//		p.add(new Production(A, A, a));
//		p.add(new Production(B, b, b));
//		p.add(new Production(B, c));
//		p.add(new Production(S, A, B));
//		p.add(new Production(A, a));
//		p.add(new Production(B, b));
		InputAlphabet input = new InputAlphabet();
//		for (char i = 'a'; i <= 'z'; i++){
//			input.add(new Symbol(Character.toString(i)));
//		}
		input.add(new Symbol(Character.toString('a')));
		input.add(new Symbol(Character.toString('b')));
		RegularExpressionGrammar gram = new RegularExpressionGrammar(input, new OperatorAlphabet());
		gram.trimAlphabets();
		
		StartVariable s = new StartVariable("S");
		
		
		BruteParser parser = new UnrestrictedBruteParser(gram);
		
		
		//parser.stepParser();
		//System.out.println(parser.isAccept());
		
//		//parser.resetParserStateOnly();
//		parser.stepParser();
//		parser.stepParser();
//		System.out.println(parser.isAccept());
		System.out.println(parser.quickParse(SymbolString.createFromString("(aba+a)", gram)));
		System.out.println(parser.getNumberOfNodes());
	}

}
