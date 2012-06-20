package test;

import java.io.File;
import java.util.List;

import preferences.JFLAPPreferences;

import debug.JFLAPDebug;

import model.automata.simulate.AutoSimulator;
import model.automata.simulate.ConfigurationChain;
import model.automata.simulate.SingleInputSimulator;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.FinalBlock;
import model.automata.turing.buildingblock.library.MoveBlock;
import model.automata.turing.buildingblock.library.MoveUntilBlock;
import model.automata.turing.buildingblock.library.MoveUntilNotBlock;
import model.automata.turing.buildingblock.library.ShiftBlock;
import model.automata.turing.buildingblock.library.WriteBlock;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.regex.RegularExpression;
import file.xml.XMLCodec;

public class BuildingBlockTesting extends TestHarness {

	@Override
	public void runTest() {
		TapeAlphabet alph = new TapeAlphabet();
		Symbol a= new Symbol("a");
		Symbol b = new Symbol("b");
		Symbol c = new Symbol("c");
		alph.addAll(a,b,c);

		BlankSymbol blank = new BlankSymbol();
		int id = 0;

		Block block = new ShiftBlock(TuringMachineMove.LEFT, alph, blank, 0);
		SymbolString input = new SymbolString(a,a,a,b,b,c,c);
		testBlock(block, input);
//
		block = new FinalBlock(alph, blank, id);
		testBlock(block, input);
//
//		block = new MoveBlock(TuringMachineMove.RIGHT, alph, blank, id);
//		testBlock(block, input);
//
		block = new MoveUntilBlock(TuringMachineMove.RIGHT, c, alph, blank, id);
		testBlock(block, input);

//		block = new MoveUntilNotBlock(TuringMachineMove.RIGHT, a, alph, blank, id);
//		testBlock(block, input);
//
//		block = new WriteBlock(c, alph, blank, id);
//		testBlock(block, input);


		
















		//
		//		outPrintln("Block TM:\n" + blockTM);
		//
		//		SingleInputSimulator sim2 = new SingleInputSimulator(blockTM,
		//				SingleInputSimulator.DEFAULT);
		//		SymbolString s = blockTM.createFromString("1111+1111", false);
		//		outPrintln("Input will be: " + s);
		//		sim.beginSimulation(s);
		//		List<ConfigurationChain> accept = null;
		//		while (sim.canStep()){
		//			JFLAPDebug.print(accept  = sim.step());
		//		}
		//		outPrintln("Accept chain: " + accept.get(0));
	}

	private void testBlock(Block block, SymbolString input) {
		outPrintln("\n\nTESTING: \n" + block.toDetailedString());
		AutoSimulator sim = new AutoSimulator(block.getTuringMachine(), 0);
		sim.beginSimulation(input);
		List<ConfigurationChain> accept = sim.getNextAccept();
		outPrintln("The result of a "+ block.toString() + " on abbcc.: \n" + 
				(accept.isEmpty() ? "failed" : trimToResult(accept.get(0))));
	}

	private String trimToResult(ConfigurationChain chain) {
		SymbolString s = chain.getLast().getStringForIndex(0);
		s = s.subList(chain.getLast().getPositionForIndex(0));
		Symbol blank = JFLAPPreferences.getTMBlankSymbol();
		while (s.getLast().equals(blank)){
			s.removeLast();
		}
		return s.toString();
	}

	@Override
	public String getTestName() {
		return "Building Block test";
	}

}
