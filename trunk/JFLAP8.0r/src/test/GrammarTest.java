package test;

import model.formaldef.components.alphabets.specific.TerminalAlphabet;
import model.formaldef.components.alphabets.specific.VariableAlphabet;
import model.grammar.Grammar;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;

public class GrammarTest {

	public static void main(String[] args) {
		TerminalAlphabet terms = new TerminalAlphabet();
		VariableAlphabet vars = new VariableAlphabet();
		ProductionSet prod = new ProductionSet();
		StartVariable var = new StartVariable();
		Grammar g  = new Grammar(terms,
									vars, 
									prod, 
									var);
		
		System.out.println(g);
	}
	
	
}
