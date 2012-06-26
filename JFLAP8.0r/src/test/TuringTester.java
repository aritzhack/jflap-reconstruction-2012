package test;

import model.automata.simulate.AutoSimulator;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.library.MoveUntilBlock;
import model.automata.turing.universal.ConvertedUniversalTM;
import model.symbols.Symbol;
import model.symbols.symbolizer.Symbolizers;

public class TuringTester {
	public static void main(String[]args){

		TapeAlphabet tapeAlph = new TapeAlphabet();
		tapeAlph.addAll(new Symbol("a"), new Symbol("b"), new Symbol("c"));
		
		MultiTapeTuringMachine tm = (MultiTapeTuringMachine) new MoveUntilBlock(TuringMachineMove.RIGHT, new Symbol("a"), tapeAlph, 0).getTuringMachine();
		System.out.println(tm);
		
		ConvertedUniversalTM uni = new ConvertedUniversalTM(tm);
		AutoSimulator sim = new AutoSimulator(uni, 0);
		sim.beginSimulation(Symbolizers.symbolize("abbbccca", uni));
		System.out.println(sim.getNextAccept());		
	}
}
