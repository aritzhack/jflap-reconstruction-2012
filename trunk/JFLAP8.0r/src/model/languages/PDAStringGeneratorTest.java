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
import model.grammar.Grammar;
import model.grammar.transform.CNFConverter;

public class PDAStringGeneratorTest {

	public static void main (String[] args) {
		
		StateSet states = new StateSet();
		InputAlphabet input = new InputAlphabet();
		StackAlphabet stack = new StackAlphabet();
		TransitionSet<PDATransition> transitions = new TransitionSet<PDATransition>();
		StartState start = new StartState();
		FinalStateSet finalStates = new FinalStateSet();
		BottomOfStackSymbol bos = new BottomOfStackSymbol();
		PushdownAutomaton pda = new PushdownAutomaton(states, 
														input, 
														stack,
														transitions, 
														start, 
														bos,
														finalStates);

//		errPrintln(UtilFunctions.createDelimitedString(Arrays.asList(pda.isComplete()),"\n"));
//		
//		errPrintln("");
		
		for (char i = 'a'; i <= 'z'; i++){
			pda.getInputAlphabet().add(new Symbol(Character.toString(i)));
			pda.getStackAlphabet().add(new Symbol(Character.toString(i)));
		}
		
		pda.setBottomOfStackSymbol(new Symbol("z"));
		
		State q0 = new State("Z0", 0);
		State q1 = new State("Z1", 1);
		State q2 = new State("Z2", 2);
		State q3 = new State("Z3", 3);

		pda.getStates().addAll(Arrays.asList(new State[]{q0,q1,q2,q3}));
		pda.setStartState(q0);
		pda.getFinalStateSet().add(q3);
		
		Terminal A = new Terminal("a");
		Terminal B = new Terminal("b");
		
		
		PDATransition t0 = new PDATransition(q0, q1, new SymbolString(A), 
				new SymbolString(bos.toSymbolObject()), new SymbolString(A,bos.toSymbolObject()));
		PDATransition t1 = new PDATransition(q1, q1, new SymbolString(A), new SymbolString(A), new SymbolString(A,A));
		PDATransition t2 = new PDATransition(q1, q2, new SymbolString(B), new SymbolString(A), new SymbolString());
		PDATransition t3 = new PDATransition(q2, q2, new SymbolString(B), new SymbolString(A), new SymbolString());
		PDATransition t4 = new PDATransition(q2, q3, new SymbolString(), 
				new SymbolString(bos.toSymbolObject()), new SymbolString());
		
		pda.getTransitions().addAll((Arrays.asList(new PDATransition[]{t0,t1,t2,t3,t4})));
		pda.trimAlphabets();
//		outPrintln(pda.toString());
//		
//		errPrintln(UtilFunctions.createDelimitedString(Arrays.asList(pda.isComplete()),"\n"));
//		
//		errPrintln("");

//		//lets try some stuff...
//				AutoSimulator sim = new AutoSimulator(pda, SingleInputSimulator.DEFAULT);
//				String in = "aabb";
//				sim.beginSimulation(SymbolString.createFromString(in, pda));
////				outPrintln("Run string: " + in + "\n\t In Language? " + !sim.getNextAccept().isEmpty());

		//convert PDA to CFG
		SteppableAlgorithm converter = new PDAtoCFGConverter(pda);
		converter.stepToCompletion();
//		
		Grammar CFG = ((PDAtoCFGConverter) converter).getConvertedGrammar();
		System.out.println(CFG.toString());
		
		CNFConverter cnfconvert = new CNFConverter(CFG);
		Grammar cnf = cnfconvert.getTransformedGrammar();
		
		
		StringGenerator gen = new StringGenerator(cnf, 2);
		gen.generateStrings();
		
//		StringGenerator gen = new StringGenerator(pda);
//		gen.generateStrings();
		
	}
	
}
