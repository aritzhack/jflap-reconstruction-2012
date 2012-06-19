package test;

import model.algorithms.conversion.autotogram.TMtoGrammarConversion;
import model.algorithms.transform.grammar.UselessProductionRemover;
import model.algorithms.transform.turing.StayOptionRemover;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.MultiTapeTMTransition;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.parsing.brute.UnrestrictedBruteParser;
import preferences.JFLAPPreferences;

public class TuringTester {
	public static void main(String[]args){
		StateSet states = new StateSet();
		states.add(new State("q0",0));
		states.add(new State("q1",1));
//		states.add(new State("q2",2));
		TapeAlphabet tapeAlph = new TapeAlphabet();
		tapeAlph.addAll(new Symbol("a"));
		BlankSymbol blank = new BlankSymbol();
		InputAlphabet inputAlph = new InputAlphabet();
		inputAlph.addAll(new Symbol("a"));
		TransitionSet<MultiTapeTMTransition> functions = new TransitionSet<MultiTapeTMTransition>();
		functions.add(new MultiTapeTMTransition(states.getStateWithID(0), states.getStateWithID(0), new Symbol("a"), new Symbol("a"), TuringMachineMove.RIGHT));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(0), states.getStateWithID(1), JFLAPPreferences.getTMBlankSymbol(), JFLAPPreferences.getTMBlankSymbol(), TuringMachineMove.LEFT));
//		functions.add(new TuringMachineTransition(states.getStateWithID(1), states.getStateWithID(2), new Symbol("b"), new Symbol("a"), TuringMachineMove.STAY));

		StartState start = new StartState(states.getStateWithID(0));
		FinalStateSet finalStates = new FinalStateSet();
		finalStates.add(states.getStateWithID(1));
		int numTapes = 1;
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine(states, tapeAlph, blank, inputAlph, functions, start, finalStates, numTapes);
		System.out.println(tm);
		
//		StayOptionRemover remover = new StayOptionRemover(tm);
//		remover.stepToCompletion();
//		System.out.println(remover.getTransformedDefinition());
		TMtoGrammarConversion converter = new TMtoGrammarConversion(tm);
		converter.stepToCompletion();
//		System.out.println(converter.getConvertedGrammar());
		UselessProductionRemover remover = new UselessProductionRemover(converter.getConvertedGrammar());
		remover.stepToCompletion();
		UnrestrictedBruteParser parser = new UnrestrictedBruteParser(remover.getTransformedGrammar());
		System.out.println(parser.quickParse(SymbolString.toSingleCharSymbols("b")));
//		System.out.println(converter.getConvertedDefinition());
	}
}
