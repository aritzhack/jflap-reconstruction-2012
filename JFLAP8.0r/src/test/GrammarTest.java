package test;

import debug.JFLAPDebug;
import errors.BooleanWrapper;
import model.automata.InputAlphabet;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.AlphabetException;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.formaldef.rules.AlphabetRule;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;
import model.grammar.transform.CNFConverter;
import model.grammar.transform.LambdaProductionRemover;
import model.grammar.transform.UnitProductionRemover;
import model.grammar.transform.UselessProductionRemover;
import model.regex.OperatorAlphabet;
import model.regex.RegularExpressionGrammar;

public class GrammarTest extends TestHarness {

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
		
		outPrintln("Initial Grammar:\n" + g.toString());
		
		//Remove lambda productions
		LambdaProductionRemover r1 = new LambdaProductionRemover(g);
		r1.stepToCompletion();
		Grammar g2 = r1.getTransformedGrammar();
		outPrintln("LAMBDA Productions removed:\n" + g2);
		
		UnitProductionRemover r2 = new UnitProductionRemover(g2);
		r2.stepToCompletion();
		g2 = r2.getTransformedGrammar();
		outPrintln("UNIT Productions removed:\n" + g2);
		
//		UselessProductionRemover r3 = new UselessProductionRemover(g2);
//		r3.stepToCompletion();
//		g2 = r3.getTransformedGrammar();
//		outPrintln("USELESS Productions removed:\n" + g2);
//
		CNFConverter r4 = new CNFConverter(g);
//		r4.stepToCompletion();
//		g2 = r4.getTransformedGrammar();
//		outPrintln("CNF Converted:\n" + g2);
		
		InputAlphabet alph = new InputAlphabet();
		alph.addAll(g.getTerminals());
		r4 = new CNFConverter(new RegularExpressionGrammar(alph,
				new OperatorAlphabet()));
		r4.stepToCompletion();
		g2 = r4.getTransformedGrammar();
		outPrintln("CNF Converted RegEx Grammar:\n" + g2);
	}

	private static void addSymbols(Alphabet alph, Symbol ... sym) {
		for (Symbol s: sym){
			try {
				alph.add(s);
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

	@Override
	public String getTestName() {
		return "GRAMMAR TEST";
	}
	
	
}
