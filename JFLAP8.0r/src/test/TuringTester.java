package test;

import debug.JFLAPDebug;
import model.automata.simulate.AutoSimulator;
import model.automata.simulate.configurations.tm.BlockTMConfiguration;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.MoveUntilBlock;
import model.automata.turing.universal.ConvertInputBlock;
import model.automata.turing.universal.ConvertedUniversalTM;
import model.automata.turing.universal.RetrieveOutputBlock;
import model.automata.turing.universal.TMtoEncodingConversion;
import model.automata.turing.universal.UniversalTuringMachine;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;

public class TuringTester {
	public static void main(String[]args){
//		StateSet states = new StateSet();
//		states.add(new State("q0",0));
//		states.add(new State("q1",1));
////		states.add(new State("q2",2));
//		TapeAlphabet tapeAlph = new TapeAlphabet();
//		tapeAlph.addAll(new Symbol("a"), new Symbol("b"), new Symbol("c"));
		BlankSymbol blank = new BlankSymbol();
//		tapeAlph.add(blank.getSymbol());
//		InputAlphabet inputAlph = new InputAlphabet();
//		inputAlph.addAll(new Symbol("a"), new Symbol("b"));
//		TransitionSet<MultiTapeTMTransition> functions = new TransitionSet<MultiTapeTMTransition>();
//		functions.add(new MultiTapeTMTransition(states.getStateWithID(0), states.getStateWithID(0), new Symbol("a"), new Symbol("b"), TuringMachineMove.RIGHT));
//		functions.add(new MultiTapeTMTransition(states.getStateWithID(0), states.getStateWithID(1), JFLAPPreferences.getTMBlankSymbol(), JFLAPPreferences.getTMBlankSymbol(), TuringMachineMove.STAY));
////		functions.add(new TuringMachineTransition(states.getStateWithID(1), states.getStateWithID(2), new Symbol("b"), new Symbol("a"), TuringMachineMove.STAY));
//
//		StartState start = new StartState(states.getStateWithID(0));
//		FinalStateSet finalStates = new FinalStateSet();
//		finalStates.add(states.getStateWithID(1));
//		int numTapes = 1;
//		MultiTapeTuringMachine tm = new MultiTapeTuringMachine(states, tapeAlph, blank, inputAlph, functions, start, finalStates, numTapes);
//		
//		MultiTapeTuringMachine tm = (MultiTapeTuringMachine) new MoveUntilBlock(TuringMachineMove.RIGHT, new Symbol("a"), tapeAlph, new BlankSymbol(), 0).getTuringMachine();
//		System.out.println(tm);
		
//		TMtoEncodingConversion converter = new TMtoEncodingConversion(tm);
//		converter.stepToCompletion();
//		System.out.println(converter.getEncoding());
		
		TapeAlphabet tapeAlph = new TapeAlphabet();
		tapeAlph.addAll(new Symbol("a"), new Symbol("b"), new Symbol("c"));
		tapeAlph.add(blank.getSymbol());
//		ConvertInputBlock block = new ConvertInputBlock(converter.getEncoding(), tapeAlph, new BlankSymbol(), 0);
//		ConvertedUniversalTM uni = new ConvertedUniversalTM(tm);
		
//		Symbol zero = new Symbol("0"), one = new Symbol("1");
//		SymbolString en = converter.getEncoding();
//		
//		SymbolString input = new SymbolString(one,one,one,zero,one,one,one,zero,one,one,one,zero,one,one,one,one,zero,one,one,one,one,zero,one,one,one,one,zero,one,one,zero);
//		SymbolString state = new SymbolString(one);
//		
//		System.out.println(input);
//		System.out.println(state);
//		
//		UniversalTuringMachine uni = new UniversalTuringMachine(false);
//		System.out.println(uni);
		Symbol zero = new Symbol("0"), one = new Symbol("1");
//		
		RetrieveOutputBlock block = new RetrieveOutputBlock(tapeAlph, blank, 0);
		SymbolString input = new SymbolString(one,zero,one,one,zero, one,one,one,zero,one,one,one,zero,one,one,one,zero,one,one,one,one,zero,one,one,one,one,zero,one,one,one,one,zero,one,one,zero);
		BlockTMConfiguration config = new BlockTMConfiguration(block.getTuringMachine(), block.getTuringMachine().getStartState(),
							32, input);
		System.out.println(block.getTuringMachine());
		AutoSimulator sim = new AutoSimulator(block.getTuringMachine(), 0);
		sim.beginSimulation(config);
		System.out.println(sim.getNextHalt());
		
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
		
//		StateSet states = new StateSet();
//		for (int i = 0; i < 5; i++) {
//			states.createAndAddState();
//		}
//
//		Symbol a = new Symbol("a"), b = new Symbol("b"), c = new Symbol("c");
//		TapeAlphabet tapeAlph = new TapeAlphabet();
//		tapeAlph.addAll(a, b, c);
//
//		BlankSymbol blank = new BlankSymbol();
//		Symbol square = blank.getSymbol();
//		InputAlphabet inputAlph = new InputAlphabet();
//		inputAlph.addAll(a, b, c);
//
//		TransitionSet<MultiTapeTMTransition> functions = new TransitionSet<MultiTapeTMTransition>();
//		functions.add(new MultiTapeTMTransition(states.getStateWithID(0),
//				states.getStateWithID(0), new Symbol[] { a, square, square },
//				new Symbol[] { a, a, square }, new TuringMachineMove[] {
//						TuringMachineMove.RIGHT, TuringMachineMove.RIGHT,
//						TuringMachineMove.STAY }));
//		functions.add(new MultiTapeTMTransition(states.getStateWithID(0),
//				states.getStateWithID(1), new Symbol[] { b, square, square },
//				new Symbol[] { b, square, b}, new TuringMachineMove[] {
//						TuringMachineMove.RIGHT, TuringMachineMove.STAY,
//						TuringMachineMove.RIGHT }));
//		functions.add(new MultiTapeTMTransition(states.getStateWithID(0),
//				states.getStateWithID(4), new Symbol[] { square, square, square },
//				new Symbol[] { square, square, square }, new TuringMachineMove[] {
//						TuringMachineMove.STAY, TuringMachineMove.LEFT,
//						TuringMachineMove.STAY }));
//		functions.add(new MultiTapeTMTransition(states.getStateWithID(1),
//				states.getStateWithID(1), new Symbol[] { b, square, square },
//				new Symbol[] { b, square, b}, new TuringMachineMove[] {
//						TuringMachineMove.RIGHT, TuringMachineMove.STAY,
//						TuringMachineMove.RIGHT }));
//		functions.add(new MultiTapeTMTransition(states.getStateWithID(1),
//				states.getStateWithID(2), new Symbol[] { c, square, square },
//				new Symbol[] { c, square, square }, new TuringMachineMove[] {
//						TuringMachineMove.STAY, TuringMachineMove.LEFT,
//						TuringMachineMove.LEFT }));
//		functions.add(new MultiTapeTMTransition(states.getStateWithID(2),
//				states.getStateWithID(2), new Symbol[] { c, a, b},
//				new Symbol[] { c, a, b}, new TuringMachineMove[] {
//						TuringMachineMove.RIGHT, TuringMachineMove.LEFT,
//						TuringMachineMove.LEFT }));
//		functions.add(new MultiTapeTMTransition(states.getStateWithID(2),
//				states.getStateWithID(3), new Symbol[] { square, square, square },
//				new Symbol[] { square, square, square }, new TuringMachineMove[] {
//						TuringMachineMove.STAY, TuringMachineMove.STAY,
//						TuringMachineMove.RIGHT }));
//		functions.add(new MultiTapeTMTransition(states.getStateWithID(4),
//				states.getStateWithID(3), new Symbol[] { square, square, square },
//				new Symbol[] { square, square, square }, new TuringMachineMove[] {
//						TuringMachineMove.STAY, TuringMachineMove.STAY,
//						TuringMachineMove.STAY }));
//		
//		StartState start = new StartState(states.getStateWithID(0));
//		FinalStateSet finalStates = new FinalStateSet();
//		finalStates.add(states.getStateWithID(3));
//		int numTapes = 3;
//
//		MultiTapeTuringMachine tm = new MultiTapeTuringMachine(states,
//				tapeAlph, blank, inputAlph, functions, start, finalStates,
//				numTapes);
//		
//		System.out.println(tm);
//		AutoSimulator simulator = new AutoSimulator(tm, 0);
//		simulator.beginSimulation(Symbolizers.symbolize("aabbcc", tapeAlph), new SymbolString(), new SymbolString());
//		System.out.println(simulator.getNextAccept().get(0));
		
		
	}
}
