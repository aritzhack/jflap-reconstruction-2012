package test;

import errors.BooleanWrapper;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.AlphabetException;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.rules.AlphabetRule;
import model.grammar.Grammar;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;

public class GrammarTest {

	static String[] Variables = {"Hello", "He(llo", "(Hel)lo)", "(Hello)"};
	static String[] Terminals = { "Hello", "(Hello)", "(moo",};
	
	public static void main(String[] args) {
		TerminalAlphabet terms = new TerminalAlphabet();
		VariableAlphabet vars = new VariableAlphabet();
		ProductionSet prod = new ProductionSet();
		StartVariable var = new StartVariable();
		Grammar g  = new Grammar(vars,
									terms,
									prod, 
									var);
		
		System.out.println(g + "\n");
		g.setVariableGrouping(new GroupingPair('(',')'));
		System.out.println(createRuleString(g.getVariables())+ "\n");
		System.out.println(createRuleString(g.getTerminals())+ "\n");
		
		addSymbols(Variables, g.getVariables());
		addSymbols(Terminals, g.getTerminals());

		System.out.println(g + "\n");

	}

	private static void addSymbols(String[] sym,Alphabet alph) {
		for (String s: sym){
			try {
				alph.add(new Symbol(s));
			} catch (AlphabetException e){
				System.err.println(e.getMessage());
			}
		}
		
	}

	private static String createRuleString(Alphabet alph) {
		String ruleString = alph.toString() + "\n" +
						"\tDescription: " + alph.getDescription() + "\n"+
						"\tRules: " + "\n";
		
		for (AlphabetRule rule: alph.getRules()){
			ruleString += "\t\t" + rule.toString() + "\n";
		}
		
		return ruleString;
	}
	
	
}
