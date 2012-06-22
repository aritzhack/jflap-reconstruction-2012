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
import model.automata.simulate.AutoSimulator;
import model.automata.simulate.SingleInputSimulator;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.MultiTapeTMTransition;
import model.grammar.parsing.brute.UnrestrictedBruteParser;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import preferences.JFLAPPreferences;

public class TuringTester {
	public static void main(String[]args){
//		StateSet states = new StateSet();
//		states.add(new State("q0",0));
//		states.add(new State("q1",1));
////		states.add(new State("q2",2));
//		TapeAlphabet tapeAlph = new TapeAlphabet();
//		tapeAlph.addAll(new Symbol("a"));
//		BlankSymbol blank = new BlankSymbol();
//		InputAlphabet inputAlph = new InputAlphabet();
//		inputAlph.addAll(new Symbol("a"));
//		TransitionSet<MultiTapeTMTransition> functions = new TransitionSet<MultiTapeTMTransition>();
//		functions.add(new MultiTapeTMTransition(states.getStateWithID(0), states.getStateWithID(0), new Symbol("a"), new Symbol("a"), TuringMachineMove.RIGHT));
//		functions.add(new MultiTapeTMTransition(states.getStateWithID(0), states.getStateWithID(1), JFLAPPreferences.getTMBlankSymbol(), JFLAPPreferences.getTMBlankSymbol(), TuringMachineMove.LEFT));
////		functions.add(new TuringMachineTransition(states.getStateWithID(1), states.getStateWithID(2), new Symbol("b"), new Symbol("a"), TuringMachineMove.STAY));
//
//		StartState start = new StartState(states.getStateWithID(0));
//		FinalStateSet finalStates = new FinalStateSet();
//		finalStates.add(states.getStateWithID(1));
//		int numTapes = 1;
//		MultiTapeTuringMachine tm = new MultiTapeTuringMachine(states, tapeAlph, blank, inputAlph, functions, start, finalStates, numTapes);
//		System.out.println(tm);
//		
////		StayOptionRemover remover = new StayOptionRemover(tm);
////		remover.stepToCompletion();
////		System.out.println(remover.getTransformedDefinition());
//		TMtoGrammarConversion converter = new TMtoGrammarConversion(tm);
//		converter.stepToCompletion();
////		System.out.println(converter.getConvertedGrammar());
//		UselessProductionRemover remover = new UselessProductionRemover(converter.getConvertedGrammar());
//		remover.stepToCompletion();
//		UnrestrictedBruteParser parser = new UnrestrictedBruteParser(remover.getTransformedGrammar());
//		System.out.println(parser.quickParse(SymbolString.toSingleCharSymbols("aa")));
////		System.out.println(converter.getConvertedDefinition());
		
		StateSet states = new StateSet();
		for (int i = 0; i < 5; i++) {
			states.createAndAddState();
		}

		Symbol a = new Symbol("a"), b = new Symbol("b"), c = new Symbol("c");
		TapeAlphabet tapeAlph = new TapeAlphabet();
		tapeAlph.addAll(a, b, c);

		BlankSymbol blank = new BlankSymbol();
		Symbol square = blank.getSymbol();
		InputAlphabet inputAlph = new InputAlphabet();
		inputAlph.addAll(a, b, c);

		TransitionSet<MultiTapeTMTransition> functions = new TransitionSet<MultiTapeTMTransition>();
		functions.add(new MultiTapeTMTransition(states.getStateWithID(0),
				states.getStateWithID(0), new Symbol[] { a, square, square },
				new Symbol[] { a, a, square }, new TuringMachineMove[] {
						TuringMachineMove.RIGHT, TuringMachineMove.RIGHT,
						TuringMachineMove.STAY }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(0),
				states.getStateWithID(1), new Symbol[] { b, square, square },
				new Symbol[] { b, square, b}, new TuringMachineMove[] {
						TuringMachineMove.RIGHT, TuringMachineMove.STAY,
						TuringMachineMove.RIGHT }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(0),
				states.getStateWithID(4), new Symbol[] { square, square, square },
				new Symbol[] { square, square, square }, new TuringMachineMove[] {
						TuringMachineMove.STAY, TuringMachineMove.LEFT,
						TuringMachineMove.STAY }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(1),
				states.getStateWithID(1), new Symbol[] { b, square, square },
				new Symbol[] { b, square, b}, new TuringMachineMove[] {
						TuringMachineMove.RIGHT, TuringMachineMove.STAY,
						TuringMachineMove.RIGHT }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(1),
				states.getStateWithID(2), new Symbol[] { c, square, square },
				new Symbol[] { c, square, square }, new TuringMachineMove[] {
						TuringMachineMove.STAY, TuringMachineMove.LEFT,
						TuringMachineMove.LEFT }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(2),
				states.getStateWithID(2), new Symbol[] { c, a, b},
				new Symbol[] { c, a, b}, new TuringMachineMove[] {
						TuringMachineMove.RIGHT, TuringMachineMove.LEFT,
						TuringMachineMove.LEFT }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(2),
				states.getStateWithID(3), new Symbol[] { square, square, square },
				new Symbol[] { square, square, square }, new TuringMachineMove[] {
						TuringMachineMove.STAY, TuringMachineMove.STAY,
						TuringMachineMove.RIGHT }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(4),
				states.getStateWithID(3), new Symbol[] { square, square, square },
				new Symbol[] { square, square, square }, new TuringMachineMove[] {
						TuringMachineMove.STAY, TuringMachineMove.STAY,
						TuringMachineMove.STAY }));
		
		StartState start = new StartState(states.getStateWithID(0));
		FinalStateSet finalStates = new FinalStateSet();
		finalStates.add(states.getStateWithID(3));
		int numTapes = 3;

		MultiTapeTuringMachine tm = new MultiTapeTuringMachine(states,
				tapeAlph, blank, inputAlph, functions, start, finalStates,
				numTapes);
		
		System.out.println(tm);
		AutoSimulator simulator = new AutoSimulator(tm, 0);
		simulator.beginSimulation(Symbolizers.symbolize("aabbcc", tapeAlph), new SymbolString(), new SymbolString());
		System.out.println(simulator.getNextAccept().get(0));
		
		
	}
}
