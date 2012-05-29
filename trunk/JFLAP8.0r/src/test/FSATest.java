package test;

import java.awt.Color;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.algorithms.SteppableAlgorithm;
import model.algorithms.conversion.autotogram.AutomatonToGrammarConversion;
import model.algorithms.conversion.autotogram.FSAVariableMapping;
import model.algorithms.conversion.autotogram.FSAtoRegGrammarConversion;
import model.algorithms.conversion.gramtoauto.GrammarToAutomatonConverter;
import model.algorithms.conversion.gramtoauto.RGtoFSAConverter;
import model.algorithms.fsa.AddTrapStateAlgorithm;
import model.algorithms.fsa.FSAtoRegularExpressionConverter;
import model.algorithms.fsa.NFAtoDFAConverter;
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

		errPrintln(UtilFunctions.createDelimitedString(Arrays.asList(fsa.isComplete()),"\n"));
		
		for (char i = 'a'; i <= 'z'; i++){
			fsa.getInputAlphabet().add(new Symbol(Character.toString(i)));
		}
		
		State q0 = new State("q0", 0);
		State q1 = new State("q1", 1);
		State q2 = new State("q2", 2);
		State q3 = new State("q3", 3);

		fsa.getStates().addAll(Arrays.asList(new State[]{q0,q1,q2,q3}));
		fsa.setStartState(q0);
		fsa.getFinalStateSet().add(q3);
		
		Symbol A = 	new Symbol("a");
		Symbol B = new Symbol("b");
		
		
		FSTransition t0 = new FSTransition(q0, q1, new SymbolString(A));
		FSTransition t5 = new FSTransition(q1, q0, new SymbolString(B));
		FSTransition t7 = new FSTransition(q0,q1, new SymbolString());
		FSTransition t1 = new FSTransition(q1, q1, new SymbolString(A));
		FSTransition t6 = new FSTransition(q1, q1, new SymbolString(B));
		FSTransition t2 = new FSTransition(q1, q2, new SymbolString(B));
		FSTransition t3 = new FSTransition(q2, q2, new SymbolString(B));
		FSTransition t4 = new FSTransition(q2, q3, new SymbolString(A));
		
		fsa.getTransitions().addAll((Arrays.asList(new FSTransition[]{t0,t1,t2,t3,t4,t5, t6, t7})));

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
		converter = new FSAtoRegularExpressionConverter(fsa);
		converter.stepToCompletion();
		RegularExpression regEx = ((FSAtoRegularExpressionConverter) converter).getResultingRegEx();
		outPrintln(regEx.toString());
		
		//CONVERT NFA to DFA
		converter = new NFAtoDFAConverter(fsa);
		System.out.println("WTF?");
		converter.stepToCompletion();
		FiniteStateAcceptor dfa = ((NFAtoDFAConverter) converter).getDFA();
		outPrintln("DFA from NFA: \n" + dfa.toString());
		
		//Add trap state
		converter = new AddTrapStateAlgorithm(dfa);
		converter.stepToCompletion();
		dfa = ((AddTrapStateAlgorithm) converter).getDFAWithTrapState();
		outPrintln("DFA with Trap State: \n" + dfa.toString());

	}

}
