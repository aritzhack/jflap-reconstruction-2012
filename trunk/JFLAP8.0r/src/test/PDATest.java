package test;

import java.awt.Color;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.algorithms.SteppableAlgorithm;
import model.algorithms.conversion.autotogram.FSAtoRegGrammarConversion;
import model.algorithms.conversion.autotogram.PDAtoCFGConverter;
import model.algorithms.conversion.gramtoauto.CFGtoPDAConverterLL;
import model.algorithms.conversion.gramtoauto.CFGtoPDAConverterLR;
import model.algorithms.conversion.gramtoauto.RGtoFSAConverter;
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
import model.automata.simulate.PDASimulator;
import model.automata.turing.TapeAlphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.transform.GrammarTransformAlgorithm;
import model.grammar.transform.UselessProductionRemover;
import model.grammar.typetest.GrammarType;
import model.util.UtilFunctions;

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
		
		State q0 = new State("Z0", 0);
		State q1 = new State("Z1", 1);
		State q2 = new State("Z2", 2);
		State q3 = new State("Z3", 3);

		pda.getStates().addAll(Arrays.asList(new State[]{q0,q1,q2,q3}));
		pda.getStartState().setTo(q0);
		pda.getFinalStateSet().add(q3);
		
		Symbol A = 	new Symbol("a");
		Symbol B = new Symbol("b");
		
		
		PDATransition t0 = new PDATransition(q0, q1, new SymbolString(A), 
				new SymbolString(bos.toSymbolObject()), new SymbolString(A,bos.toSymbolObject()));
		PDATransition t1 = new PDATransition(q1, q1, new SymbolString(A), new SymbolString(A), new SymbolString(A,A));
		PDATransition t2 = new PDATransition(q1, q2, new SymbolString(B), new SymbolString(A), new SymbolString());
		PDATransition t3 = new PDATransition(q2, q2, new SymbolString(B), new SymbolString(A), new SymbolString());
		PDATransition t4 = new PDATransition(q2, q3, new SymbolString(), 
				new SymbolString(bos.toSymbolObject()), new SymbolString());
		
		pda.getTransitions().addAll((Arrays.asList(new PDATransition[]{t0,t1,t2,t3,t4})));

		OutPrintln(pda.toString());
		
		ErrPrintln(UtilFunctions.createDelimitedString(Arrays.asList(pda.isComplete()),"\n"));
		
		ErrPrintln("");
		
		//lets try some stuff...
				PDASimulator sim = new PDASimulator(pda);
				String in = "aaaaabbbbb";
				OutPrintln("Run string: " + in + "\n\t In Language? " + sim.acceptsInput(in));
		
		//convert PDA to CFG
		SteppableAlgorithm converter = new PDAtoCFGConverter(pda);
		while (converter.step());
		
		Grammar CFG = ((PDAtoCFGConverter) converter).getConvertedGrammar();
		
		OutPrintln(CFG.toString());
		//remove useless productions
		converter = new UselessProductionRemover(CFG);
		converter.stepToCompletion();
		
		CFG = ((GrammarTransformAlgorithm) converter).getTransformedGrammar();
		OutPrintln("No Useless productions: \n" + CFG.toString());
		
		//Now trim
		CFG.trimAlphabets();
		OutPrintln("Alphabets Trimmed: \n" + CFG.toString());
		
		//TYPE TEST
		OutPrintln(Arrays.toString(GrammarType.getType(CFG)));
		
		converter = new CFGtoPDAConverterLL(CFG);
		while (converter.step()){
		}
		pda = ((CFGtoPDAConverterLL) converter).getConvertedAutomaton();
		
		OutPrintln("LL CONVERTED:\n" + pda.toString());

		
		sim = new PDASimulator(pda);
		OutPrintln("Run string: " + in + "\n\t In Language? " + sim.acceptsInput(in));
		
		converter = new CFGtoPDAConverterLR(CFG);
		while (converter.step()){
		}
		pda = ((CFGtoPDAConverterLR) converter).getConvertedAutomaton();
		
		OutPrintln("LR CONVERTED:\n" + pda.toString());
		
		sim = new PDASimulator(pda);
		OutPrintln("Run string: " + in + "\n\t In Language? " + sim.acceptsInput(in));
		
		pda.getStackAlphabet().remove(new Symbol("a"));
		
		OutPrintln("\'a\' removed:\n" + pda.toString());


		pda.getStates().remove(pda.getStates().getStateWithID(1));
		
		OutPrintln("[q1] removed:\n" + pda.toString());
		
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