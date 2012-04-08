package test;

import java.awt.Color;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.algorithms.conversion.autotogram.FSAtoRegGrammarConversion;
import model.algorithms.conversion.autotogram.PDAtoCFGConverter;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.acceptors.pda.StackAlphabet;
import model.automata.turing.TapeAlphabet;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.typetest.GrammarType;
import util.UtilFunctions;

public class PDATest {

	private static JTextArea myArea;

	public static void main(String[] args) {
		myArea = setUpDisplay();
		StateSet states = new StateSet();
		InputAlphabet input = new InputAlphabet();
		StackAlphabet stack = new StackAlphabet();
		TransitionFunctionSet<PDATransition> transitions = new TransitionFunctionSet<PDATransition>();
		StartState start = new StartState();
		FinalStateSet finalStates = new FinalStateSet();
		BottomOfStackSymbol bos = new BottomOfStackSymbol("z");
		PushdownAutomaton pda = new PushdownAutomaton(states, 
														input, 
														stack,
														transitions, 
														start, 
														bos,
														finalStates);

		ErrPrintln(UtilFunctions.createDelimitedString(Arrays.asList(pda.isComplete()),"\n"));
		
		ErrPrintln("");
		
		for (char i = 'a'; i <= 'y'; i++){
			pda.getInputAlphabet().add(new Symbol(Character.toString(i)));
			pda.getStackAlphabet().add(new Symbol(Character.toString(i)));
		}
		
		State q0 = new State("Z0", 0, null);
		State q1 = new State("Z1", 1, null);
		State q2 = new State("Z2", 2, null);
		State q3 = new State("Z3", 3, null);

		pda.getStates().addAll(Arrays.asList(new State[]{q0,q1,q2,q3}));
		pda.getStartState().setTo(q0);
		pda.getFinalStateSet().add(q3);
		
		Symbol A = 	new Symbol("a");
		Symbol B = new Symbol("b");
		
		
		PDATransition t0 = new PDATransition(q0, q1, new SymbolString(A), bos, new SymbolString(A,bos));
		PDATransition t1 = new PDATransition(q1, q1, new SymbolString(A), A, new SymbolString(A,A));
		PDATransition t2 = new PDATransition(q1, q2, new SymbolString(B), A, new SymbolString());
		PDATransition t3 = new PDATransition(q2, q2, new SymbolString(B), A, new SymbolString());
		PDATransition t4 = new PDATransition(q2, q3, new SymbolString(), bos, new SymbolString());
		
		pda.getTransitions().addAll((Arrays.asList(new PDATransition[]{t0,t1,t2,t3,t4})));

		OutPrintln(pda.toString());
		
		ErrPrintln(UtilFunctions.createDelimitedString(Arrays.asList(pda.isComplete()),"\n"));
		
		ErrPrintln("");
		
		PDAtoCFGConverter converter = new PDAtoCFGConverter(pda);
		while (converter.step());
		
		Grammar CFG = converter.getConvertedGrammar();
		
		OutPrintln(CFG.toString());
		
		OutPrintln(Arrays.toString(GrammarType.getType(CFG)));


	}

	private static void OutPrintln(String s) {
		myArea.setForeground(Color.BLACK);
		myArea.append(s + "\n");
	}

	private static void ErrPrintln(String str) {
		myArea.setForeground(Color.red);
		myArea.append(str +"\n");
		
	}

	private static JTextArea setUpDisplay() {
		JFrame frame = new JFrame("JFLAP Test Print!");
		JTextArea area = new JTextArea();
		JScrollPane panel = new JScrollPane(area);

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		return area;
	}
}