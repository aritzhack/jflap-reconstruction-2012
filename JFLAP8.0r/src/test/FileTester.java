package test;

import java.io.File;
import java.util.Arrays;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FSTransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.util.UtilFunctions;

import file.XMLFileChooser;
import file.xml.XMLCodec;

public class FileTester extends TestHarness {

	@Override
	public void runTest() {
		FiniteStateAcceptor fsa = createFSA();
		outPrintln(fsa.toString());
		
		XMLFileChooser chooser = new XMLFileChooser();
		chooser.showSaveDialog(null);
		File f = chooser.getSelectedFile();
		if (f == null) return;
		outPrintln(f.toString());

		XMLCodec codec = new XMLCodec();
		codec.encode(fsa, f, null);
		
		fsa = (FiniteStateAcceptor) codec.decode(f);
		
		outPrintln("After import:\n" + fsa.toString());
	}

	private FiniteStateAcceptor createFSA() {
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
		return fsa;
		
	}

	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
