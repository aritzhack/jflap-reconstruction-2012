package model.languages;

import java.util.Arrays;

import model.algorithms.SteppableAlgorithm;
import model.algorithms.conversion.autotogram.PDAtoCFGConverter;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.acceptors.pda.StackAlphabet;
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
import model.util.JFLAPConstants;
import model.util.UtilFunctions;

public class GeneratorTest {

	
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
//		p.add(new Production(S, a, S, b));
//		p.add(new Production(S, a, b));
//		p.add(new Production(S, new SymbolString()));
//		
//
//		StartVariable s = new StartVariable("S");
//		
//		Grammar g = new Grammar(v, t, p, s);
//		StringGenerator gen = new StringGenerator(g);
//		gen.generateStrings();
//		
		InputAlphabet input = new InputAlphabet();
		input.add(new Symbol(Character.toString('0')));
		input.add(new Symbol(Character.toString('1')));
		
		RegularExpressionGrammar gram = new RegularExpressionGrammar(input, new OperatorAlphabet());
		gram.trimAlphabets();
		
		StringGenerator gen = new StringGenerator(gram, 12);
		gen.generateStrings();
		
		
	}
}
