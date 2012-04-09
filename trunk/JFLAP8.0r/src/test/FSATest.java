package test;

import java.awt.Color;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import util.UtilFunctions;
import model.algorithms.SteppableAlgorithm;
import model.algorithms.conversion.autotogram.AutomatonToGrammarConversion;
import model.algorithms.conversion.autotogram.FSAVariableMapping;
import model.algorithms.conversion.autotogram.FSAtoRegGrammarConversion;
import model.algorithms.conversion.gramtoauto.GrammarToAutomatonConverter;
import model.algorithms.conversion.gramtoauto.RGtoFSAConverter;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FiniteStateTransition;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;
import model.formaldef.components.functionset.FunctionSet;
import model.grammar.Grammar;
import model.grammar.typetest.GrammarType;

public class FSATest {

	
	private static JTextArea myArea;

	public static void main(String[] args) {
		myArea = setUpDisplay();
		StateSet states = new StateSet();
		InputAlphabet input = new InputAlphabet();
		TransitionFunctionSet transitions = new TransitionFunctionSet();
		StartState start = new StartState();
		FinalStateSet finalStates = new FinalStateSet();
		
		FiniteStateAcceptor fsa = new FiniteStateAcceptor(states, 
															input, 
															transitions, 
															start, 
															finalStates);

		ErrPrintln(UtilFunctions.createDelimitedString(Arrays.asList(fsa.isComplete()),"\n"));
		
		for (char i = 'a'; i <= 'z'; i++){
			fsa.getInputAlphabet().add(new Symbol(Character.toString(i)));
		}
		
		State q0 = new State("moo", 0, null);
		State q1 = new State("mar", 1, null);
		State q2 = new State("doo", 2, null);
		State q3 = new State("eat", 3, null);

		fsa.getStates().addAll(Arrays.asList(new State[]{q0,q1,q2,q3}));
		fsa.getStartState().setTo(q0);
		fsa.getFinalStateSet().add(q3);
		
		Symbol A = 	new Symbol("a");
		Symbol B = new Symbol("b");
		
		
		FiniteStateTransition t0 = new FiniteStateTransition(q0, q1, new SymbolString(A));
		FiniteStateTransition t1 = new FiniteStateTransition(q1, q1, new SymbolString(A));
		FiniteStateTransition t2 = new FiniteStateTransition(q1, q2, new SymbolString(B));
		FiniteStateTransition t3 = new FiniteStateTransition(q2, q2, new SymbolString(B));
		FiniteStateTransition t4 = new FiniteStateTransition(q2, q3, new SymbolString(A));
		
		fsa.getTransitions().addAll((Arrays.asList(new FiniteStateTransition[]{t0,t1,t2,t3,t4})));

		OutPrintln(fsa.toString());
		
		// CONVERT FSA TO GRAMMAR
		SteppableAlgorithm converter = new FSAtoRegGrammarConversion(fsa);
		while (converter.step());
		
		Grammar RG = ((FSAtoRegGrammarConversion) converter).getConvertedGrammar();
		
		OutPrintln(RG.toString());
		
		//CHECK TYPE
		OutPrintln(Arrays.toString(GrammarType.getType(RG)));

		
		//CONVERT RIGHT-LINEAR to FSA
		converter = new RGtoFSAConverter(RG);
		while (converter.step()){
		}
		fsa = ((RGtoFSAConverter) converter).getConvertedAutomaton();
		OutPrintln(fsa.toString());
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
