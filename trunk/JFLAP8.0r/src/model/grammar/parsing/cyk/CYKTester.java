/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */




package model.grammar.parsing.cyk;

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

//import jflap.model.grammar.Grammar;
//import jflap.model.grammar.Production;
//import jflap.model.grammar.UnrestrictedGrammar;

/**
 * CYK Parser tester.
 * @author Kyung Min (Jason) Lee
 *
 */
public class CYKTester {

	public static void main(String[] args)
	{
//		Production[] productions=new Production[10];
//		productions[0]=new Production("S","AD");
//		productions[1]=new Production("D","SC");
//		productions[2]=new Production("A","a");
//		productions[3]=new Production("C","b");
//		productions[4]=new Production("S","CB");
//		productions[5]=new Production("B","CE");
//		productions[6]=new Production("E","CB");
//		productions[7]=new Production("S","SS");
//		productions[8]=new Production("S","b");
//		productions[9]=new Production("B","CC");
//		
//		Grammar g=new UnrestrictedGrammar();
//		g.addProductions(productions);
//		g.setStartVariable("S");
//		String target="abbbb";
//		System.out.println("aa");
//		CYKParserOld parser=new CYKParserOld(g);
//		System.out.println(parser.solve(target));
//		System.out.println("Trace = "+parser.getTrace());
		
		VariableAlphabet variables = new VariableAlphabet();
		variables.addAll(new Symbol("S"), new Symbol("D"), new Symbol("A"), new Symbol("C"), new Symbol("B"));
		TerminalAlphabet terminals = new TerminalAlphabet();
		terminals.addAll(new Symbol("a"), new Symbol("b"));
		ProductionSet functions = new ProductionSet();
		functions.add(new Production(new Variable("S"), new SymbolString(new Variable("B"), new Variable("A"))));
		functions.add(new Production(new Variable("S"), new SymbolString(new Terminal("a"))));
		functions.add(new Production(new Variable("A"), new SymbolString(new Variable("B"), new Variable("D"))));
		functions.add(new Production(new Variable("D"), new SymbolString(new Variable("A"), new Variable("C"))));
		functions.add(new Production(new Variable("A"), new SymbolString(new Variable("A"), new Variable("A"))));
		functions.add(new Production(new Variable("A"), new SymbolString(new Variable("B"), new Variable("C"))));
		functions.add(new Production(new Variable("C"), new Terminal("b")));
		functions.add(new Production(new Variable("A"), new Terminal("a")));
		functions.add(new Production(new Variable("A"), new SymbolString(new Variable("B"), new Variable("A"))));
		functions.add(new Production(new Variable("B"), new Terminal("a")));
		StartVariable startVar = new StartVariable("S");
		Grammar g = new Grammar(variables, terminals, functions, startVar);
		CYKParser parser = new CYKParser(g);
		Symbol a = new Terminal("a");
		Symbol b = new Terminal("b");
		SymbolString target = new SymbolString(a,a,b,a,b);
		System.out.println(parser.parse(target));
		System.out.println(parser.getTrace());
	}
}
