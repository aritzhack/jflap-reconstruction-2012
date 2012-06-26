package test;

import java.io.File;

import model.automata.State;
import model.automata.TransitionSet;
import model.automata.simulate.AutoSimulator;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachineMove;
import model.symbols.Symbol;
import model.symbols.symbolizer.Symbolizers;
import debug.JFLAPDebug;
import file.xml.XMLCodec;

public class TuringTester {
	//Hey Julian, I wrote this with the file stuff so I don't have to write them again,
	//but you should check out the last one I did. it's from pg 134 of the JFLAP book,
	//and is very nondeterministic, and it looks to me like the printout of the accept
	//isn't complete (though it still accepts it). Something to check.
	
	public static void main(String[]args){
		String toSave = System.getProperties().getProperty("user.dir")
				+ "/filetest";
		
		MultiTapeTuringMachine tm = createLangacc_a();
		
		File f = new File(toSave + "/ex9.5langacc-a.jff");
		JFLAPDebug.print("Before import:\n" + tm.toString());
		XMLCodec codec = new XMLCodec();
		codec.encode(tm, f, null);
		tm = (MultiTapeTuringMachine) codec.decode(f);
		JFLAPDebug.print("After import:\n" + tm.toString());
		
		f = new File(toSave + "/ex9.5langacc-b.jff");
		tm = createLangacc_b();
		JFLAPDebug.print("Before import:\n" + tm.toString());
		codec.encode(tm, f, null);
		tm = (MultiTapeTuringMachine) codec.decode(f);
		JFLAPDebug.print("After import:\n" + tm.toString());
		
		f = new File(toSave + "/ex9.5trans-a.jff");
		tm = createTrans_a();
		JFLAPDebug.print("Before import:\n" + tm.toString());
		codec.encode(tm, f, null);
		tm = (MultiTapeTuringMachine) codec.decode(f);
		JFLAPDebug.print("After import:\n" + tm.toString());
		
		f = new File(toSave + "/ex9.5ttm-whatlang-a.jff");
		tm = createwhatlang_a();
		JFLAPDebug.print("Before import:\n" + tm.toString());
		codec.encode(tm, f, null);
		tm = (MultiTapeTuringMachine) codec.decode(f);
		JFLAPDebug.print("After import:\n" + tm.toString());
		
		f = new File(toSave + "/ex9.5ttm-whattran-a.jff");
		tm = createwhattran_a();
		JFLAPDebug.print("Before import:\n" + tm.toString());
		codec.encode(tm, f, null);
		tm = (MultiTapeTuringMachine) codec.decode(f);
		JFLAPDebug.print("After import:\n" + tm.toString());
		
		f = new File(toSave + "/ex9.2tape-substring.jff");
		tm = createTape_substring();
		JFLAPDebug.print("Before import:\n" + tm.toString());
		codec.encode(tm, f, null);
		tm = (MultiTapeTuringMachine) codec.decode(f);
		JFLAPDebug.print("After import:\n" + tm.toString());
	}
	
	private static MultiTapeTuringMachine createLangacc_a() {
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine();
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		Symbol a = new Symbol("a"), X = new Symbol("X"), b = new Symbol("b");
		TuringMachineMove R= TuringMachineMove.RIGHT, L=TuringMachineMove.LEFT;
		int id=0;
		
		State[] q = new State[8];
		for(int i=0; i<8; i++){
			q[i] = new State("q"+i, i);
		}
		
		tm.setStartState(q[0]);
		addTransition(transitions, q[0], q[1], a, X, R);
		addTransition(transitions, q[1], q[1], a, a, R);
		addTransition(transitions, q[1], q[2], b, X, R);
		addTransition(transitions, q[1], q[4], X, X, R);
		addTransition(transitions, q[4], q[4], X, X, R);
		addTransition(transitions, q[4], q[2], b, X, R);
		addTransition(transitions, q[2], q[3], b, X, L);
		addTransition(transitions, q[3], q[3], X, X, L);
		addTransition(transitions, q[3], q[5], a, a, L);
		addTransition(transitions, q[5], q[5], a, a, L);
		addTransition(transitions, q[5], q[0], X, X, R);
		addTransition(transitions, q[3], q[6], tm.getBlankSymbol(), tm.getBlankSymbol(), R);
		addTransition(transitions, q[6], q[6], X, X, R);
		addTransition(transitions, q[6], q[7], tm.getBlankSymbol(), tm.getBlankSymbol(), R);
		tm.getFinalStateSet().add(q[7]);
		
		return tm;
	}
	
	private static MultiTapeTuringMachine createLangacc_b(){
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine();
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		Symbol a = new Symbol("a"), X = new Symbol("X"), b = new Symbol("b"),
				blank = tm.getBlankSymbol();
		TuringMachineMove R= TuringMachineMove.RIGHT, L=TuringMachineMove.LEFT;
		int id=0;
		
		State[] q = new State[5];
		for(int i=0; i<5; i++){
			q[i] = new State("q"+i, i);
		}
		
		tm.setStartState(q[0]);
		tm.getFinalStateSet().add(q[3]);
		addTransition(transitions, q[0], q[0], a, a, R);
		addTransition(transitions, q[0], q[0], X, X, R);
		addTransition(transitions, q[0], q[1], b, X, R);
		addTransition(transitions, q[1], q[1], b, b, R);
		addTransition(transitions, q[1], q[1], X, X, R);
		addTransition(transitions, q[1], q[2], blank, blank, L);
		addTransition(transitions, q[1], q[4], a, X, L);
		addTransition(transitions, q[2], q[2], X, X, L);
		addTransition(transitions, q[2], q[2], b, b, L);
		addTransition(transitions, q[2], q[3], blank, blank, R);
		addTransition(transitions, q[2], q[4], a, X, L);
		addTransition(transitions, q[4], q[4], a, a, L);
		addTransition(transitions, q[4], q[4], b, b, L);
		addTransition(transitions, q[4], q[4], X, X, L);
		addTransition(transitions, q[4], q[0], blank, blank, R);
		
		return tm;
	}
	
	private static MultiTapeTuringMachine createTrans_a(){
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine();
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		Symbol a = new Symbol("a"), X = new Symbol("X"), b = new Symbol("b"),
				blank = tm.getBlankSymbol();
		TuringMachineMove R= TuringMachineMove.RIGHT, L=TuringMachineMove.LEFT;
		int id=0;
		
		State[] q = new State[4];
		for(int i=0; i<4; i++){
			q[i] = new State("q"+i, i);
		}
		
		tm.setStartState(q[0]);
		tm.getFinalStateSet().add(q[3]);
		
		addTransition(transitions, q[0], q[0], b, b, R);
		addTransition(transitions, q[0], q[1], a, a, R);
		addTransition(transitions, q[0], q[2], blank, blank, L);
		addTransition(transitions, q[1], q[0], a, b, R);
		addTransition(transitions, q[1], q[0], b, b, R);
		addTransition(transitions, q[1], q[2], blank, blank, L);
		addTransition(transitions, q[2], q[2], b, b, L);
		addTransition(transitions, q[2], q[2], a, a, L);
		addTransition(transitions, q[2], q[3], blank, blank, R);
		
		return tm;
	}
	
	private static MultiTapeTuringMachine createwhatlang_a(){
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine(2);
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		Symbol a = new Symbol("a"), b = new Symbol("b"),
				blank = tm.getBlankSymbol();
		TuringMachineMove R= TuringMachineMove.RIGHT, L=TuringMachineMove.LEFT,
				S = TuringMachineMove.STAY;
			
		State[] q = new State[4];
		for(int i=0; i<4; i++){
			q[i] = new State("q"+i, i);
		}
		
		tm.setStartState(q[0]);
		tm.getFinalStateSet().add(q[3]);
		add2TapeTrans(transitions, q[0], q[0], a, a, R, blank, a, R);
		add2TapeTrans(transitions, q[0], q[1], b, b, S, blank, blank, L);
		add2TapeTrans(transitions, q[1], q[2], b, b, R, a, a, S);
		add2TapeTrans(transitions, q[2], q[1], b, b, R, a, a, L);
		add2TapeTrans(transitions, q[1], q[3], blank, blank, R, blank, blank, R);

		return tm;
	}
	
	private static MultiTapeTuringMachine createwhattran_a(){
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine(2);
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		Symbol one = new Symbol("1"),
				blank = tm.getBlankSymbol();
		TuringMachineMove R= TuringMachineMove.RIGHT, L=TuringMachineMove.LEFT,
				S = TuringMachineMove.STAY;
			
		State[] q = new State[7];
		for(int i=0; i<7; i++){
			q[i] = new State("q"+i, i);
		}
		
		tm.setStartState(q[0]);
		tm.getFinalStateSet().add(q[6]);
		
		add2TapeTrans(transitions, q[0], q[1], one, one, S, blank, one, R);
		add2TapeTrans(transitions, q[1], q[2], one, one, S, blank, one, R);
		add2TapeTrans(transitions, q[2], q[3], one, one, S, blank, one, R);
		add2TapeTrans(transitions, q[3], q[0], one, one, R, blank, one, R);
		add2TapeTrans(transitions, q[0], q[4], blank, blank, R, blank, one, R);
		add2TapeTrans(transitions, q[4], q[5], blank, blank, R, blank, one, L);
		add2TapeTrans(transitions, q[5], q[5], blank, blank, S, one, one, L);
		add2TapeTrans(transitions, q[5], q[6], blank, blank, R, blank, blank, L);
		
		return tm;
		
	}
	
	private static MultiTapeTuringMachine createTape_substring(){
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine(2);
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		Symbol a = new Symbol("a"), b = new Symbol("b"),
				blank = tm.getBlankSymbol();
		TuringMachineMove R= TuringMachineMove.RIGHT, L=TuringMachineMove.LEFT,
				S = TuringMachineMove.STAY;
			
		State[] q = new State[3];
		for(int i=0; i<3; i++){
			q[i] = new State("q"+i, i);
		}
		
		tm.setStartState(q[0]);
		tm.getFinalStateSet().add(q[2]);
		
		add2TapeTrans(transitions, q[0], q[0], b, b, S, b, b, R);
		add2TapeTrans(transitions, q[0], q[0], a, a, S, b, b, R);
		add2TapeTrans(transitions, q[0], q[0], b, b, S, a, a, R);
		add2TapeTrans(transitions, q[0], q[0], a, a, S, a, a, R);
		add2TapeTrans(transitions, q[0], q[1], b, b, R, b, b, R);
		add2TapeTrans(transitions, q[0], q[1], a, a, R, a, a, R);
		add2TapeTrans(transitions, q[0], q[2], blank, blank, S, blank, blank, S);
		add2TapeTrans(transitions, q[0], q[2], blank, blank, S, a, a, R);
		add2TapeTrans(transitions, q[0], q[2], blank, blank, S, b, b, R);
		add2TapeTrans(transitions, q[1], q[1], b, b, R, b, b, R);
		add2TapeTrans(transitions, q[1], q[1], a, a, R, a, a, R);
		add2TapeTrans(transitions, q[1], q[2], blank, blank, S, blank, blank, S);
		add2TapeTrans(transitions, q[1], q[2], blank, blank, S, a, a, R);
		add2TapeTrans(transitions, q[1], q[2], blank, blank, S, b, b, R);

		AutoSimulator sim = new AutoSimulator(tm, 0);
		sim.beginSimulation(Symbolizers.symbolize("abaa"), Symbolizers.symbolize("ababaabaab"));
		JFLAPDebug.print(sim.getNextAccept());
		
		return tm;
	
	}

	private static void addTransition(TransitionSet<MultiTapeTMTransition> transitions, State from, State to, Symbol read, Symbol write, TuringMachineMove move){
		transitions.add(new MultiTapeTMTransition(from, to, read, write, move));
	}
	
	private static void add2TapeTrans(TransitionSet<MultiTapeTMTransition> transitions, State from, State to, Symbol r1, Symbol w1, TuringMachineMove m1, Symbol r2, Symbol w2, TuringMachineMove m2){
		transitions.add(new MultiTapeTMTransition(from, to, new Symbol[]{r1,r2}, new Symbol[]{w1,w2}, new TuringMachineMove[]{m1,m2}));
	}
}
