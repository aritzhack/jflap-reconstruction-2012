package test;

import java.awt.Color;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import debug.JFLAPDebug;

import model.algorithms.SteppableAlgorithm;
import model.algorithms.conversion.autotogram.AutomatonToGrammarConversion;
import model.algorithms.conversion.autotogram.FSAVariableMapping;
import model.algorithms.conversion.autotogram.FSAtoRegGrammarConversion;
import model.algorithms.conversion.gramtoauto.GrammarToAutomatonConverter;
import model.algorithms.conversion.gramtoauto.RGtoFSAConverter;
import model.algorithms.fsa.AddTrapStateAlgorithm;
import model.algorithms.fsa.DFAtoRegularExpressionConverter;
import model.algorithms.fsa.InacessibleStateRemover;
import model.algorithms.fsa.NFAtoDFAConverter;
import model.algorithms.fsa.minimizer.MinimizeDFAAlgorithm;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FSTransition;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.grammar.Grammar;
import model.grammar.typetest.GrammarType;
import model.regex.RegularExpression;
import model.util.UtilFunctions;

public class FSATest extends TestHarness{

	
	@Override
	public void runTest(){
		StateSet states = new StateSet();
		InputAlphabet input = new InputAlphabet();
		TransitionSet transitions = new TransitionSet();
		StartState start = new StartState();
		FinalStateSet finalStates = new FinalStateSet();
		
		FiniteStateAcceptor fsa = new FiniteStateAcceptor(states, 
															input, 
															transitions, 
															start, 
															finalStates);
		outPrintln("Testing error/definition completion printouts:");
		errPrintln(UtilFunctions.createDelimitedString(Arrays.asList(fsa.isComplete()),"\n") + "\n");
		
		for (char i = '0'; i <= '9'; i++){
			fsa.getInputAlphabet().add(new Symbol(Character.toString(i)));
		}
		
		//figure 2.18 from the linz book with minor adjustments for non-determinism
		State q0 = new State("q0", 0);
		State q1 = new State("q1", 1);
		State q2 = new State("q2", 2);
		State q3 = new State("q3", 3);
		State q4 = new State("q4", 4);


		fsa.getStates().addAll(Arrays.asList(new State[]{q0,q1,q2,q3,q4}));
		fsa.setStartState(q0);
		fsa.getFinalStateSet().addAll(Arrays.asList(new State[]{q2,q4}));
		
		Symbol ONE = new Terminal("1");
		Symbol ZERO = new Terminal("0");
		
		FSTransition t0 = new FSTransition(q0, q1, new SymbolString(ZERO));
		FSTransition t1 = new FSTransition(q0, q3, new SymbolString(ONE));
		FSTransition t2 = new FSTransition(q1, q2, new SymbolString(ZERO));
		FSTransition t3 = new FSTransition(q1, q4, new SymbolString(ONE));
		FSTransition t4 = new FSTransition(q2, q1, new SymbolString(ZERO));
		FSTransition t5 = new FSTransition(q2, q4, new SymbolString(ONE));
		FSTransition t6 = new FSTransition(q3,q2, new SymbolString(ZERO));
		FSTransition t7 = new FSTransition(q3, q4, new SymbolString(ONE));
		FSTransition t8 = new FSTransition(q4, q4, new SymbolString(ONE));
		FSTransition t9 = new FSTransition(q4, q4, new SymbolString(ZERO));

		
		fsa.getTransitions().addAll((Arrays.asList(new FSTransition[]{t0,t1,t2,t3,t4,t5,t6,t7,t8,t9})));

		fsa.trimAlphabets();
		
		outPrintln(fsa.toString());
		
		// CONVERT FSA TO GRAMMAR
		SteppableAlgorithm converter = new FSAtoRegGrammarConversion(fsa);
		while (converter.step());
		
		Grammar RG = ((FSAtoRegGrammarConversion) converter).getConvertedGrammar();
		
		outPrintln(RG.toString());
		
		//CHECK TYPE
		outPrintln(Arrays.toString(GrammarType.getType(RG)));

		
		//CONVERT RIGHT-LINEAR to FSA
		converter = new RGtoFSAConverter(RG);
		converter.stepToCompletion();
		FiniteStateAcceptor converted = 
				((RGtoFSAConverter) converter).getConvertedAutomaton();
		outPrintln(converted.toString());
		
		//CONVERT FSA to RE
		converter = new DFAtoRegularExpressionConverter(fsa);
		converter.stepToCompletion();
		RegularExpression regEx = ((DFAtoRegularExpressionConverter) converter).getResultingRegEx();
		outPrintln("Regex from FSA:\n" + regEx.toString());
		
		//CONVERT NFA to DFA
		FiniteStateAcceptor nfa = fsa.copy();
		nfa.getTransitions().add(new FSTransition(q0, q4, new SymbolString(ONE)));
		converter = new NFAtoDFAConverter(nfa);
		converter.stepToCompletion();
		FiniteStateAcceptor dfa = ((NFAtoDFAConverter) converter).getDFA();
		outPrintln("DFA from NFA: \n" + dfa.toString());
		
		//minimize dfa Testing - test each step and then the whole alg.
		// FIRST: add some inacessible states
		dfa = fsa.copy();
		State q5 = dfa.getStates().createAndAddState();
		State q6 = dfa.getStates().createAndAddState();
		dfa.getTransitions().add(new FSTransition(q5, q4, new SymbolString(ONE)));

		outPrintln("Base FSA for minimization testing:\n" + dfa.toString());
		
		converter = new InacessibleStateRemover(dfa);
		converter.stepToCompletion();
		outPrintln("FSA with Inaccessible states removed: \n" + 
		((InacessibleStateRemover) converter).getAdjustedAutomaton().toString());
		
		//Add trap state
		converter = new AddTrapStateAlgorithm(dfa);
		converter.stepToCompletion();
		outPrintln("DFA with Trap State: \n" + 
		((AddTrapStateAlgorithm) converter).getDFAWithTrapState().toString());
		
		converter = new MinimizeDFAAlgorithm(dfa);
		converter.stepToCompletion();
		dfa = ((MinimizeDFAAlgorithm) converter).getMinimizedDFA();
		outPrintln("MinimizedDFA: \n" + dfa.toString());

	}

	@Override
	public String getTestName() {
		return "FSA TEST";
	}

}