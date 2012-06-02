package test;

import java.util.regex.Pattern;

import model.algorithms.conversion.regextofa.RegularExpressionToNFAConversion;
import model.algorithms.fsa.DFAtoRegularExpressionConverter;
import model.algorithms.fsa.NFAtoDFAConverter;
import model.algorithms.fsa.minimizer.MinimizeDFAAlgorithm;
import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FSTransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.simulate.AutoSimulator;
import model.automata.simulate.AutomatonSimulator;
import model.automata.simulate.SingleInputSimulator;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.regex.RegularExpression;

public class RegExTest extends TestHarness{


	@Override
	public void runTest() {
		InputAlphabet input = new InputAlphabet();
		RegularExpression regex = new RegularExpression(input);

		for (char i = 'a'; i <= 'z'; i++){
			regex.getInputAlphabet().add(new Symbol(Character.toString(i)));
		}

		outPrintln(regex.toString());

		//try removing a symbol
		regex.getInputAlphabet().remove(new Symbol("a"));
		outPrintln("Removed a: \n" + regex.toString());

		//try adding a symbol
		regex.getInputAlphabet().add(new Symbol("a"));
		outPrintln("Add a: \n" + regex.toString());

		//set regex
		String in = "((a+b)*+c)";
		regex.setTo(in);
		outPrintln("RegEx set to " + in + ": \n" + regex.toString());

		//trim alphabets
		regex.trimAlphabets();
		
		//try matching!
		boolean matches = regex.matches(in = "aaaaaaaab");
		outPrintln("RegEx matches " + in + ": \n" + matches);

		//test against actual java object
		matches = Pattern.matches("[ab]*|c", in);
		outPrintln("Java RegEx matches " + in + ": \n" + matches);


		//convert to NFA
		RegularExpressionToNFAConversion converter = new RegularExpressionToNFAConversion(regex);
		converter.stepToCompletion();
		FiniteStateAcceptor fsa = converter.getCompletedNFA();
		outPrintln(fsa.toString());

		//convert to DFA for conversion back to rex
		NFAtoDFAConverter c2 = new NFAtoDFAConverter(fsa);
		c2.stepToCompletion();
		FiniteStateAcceptor dfa = c2.getDFA();
		outPrintln("NFA converted to DFA:\n" + dfa.toString());
		
		//test input on DFA to confirm equivalence
		AutoSimulator sim = new AutoSimulator(dfa, SingleInputSimulator.DEFAULT);
		sim.beginSimulation(SymbolString.createFromString(in, dfa));
		outPrintln("Run string: " + in + "\n\t In Language? " + !sim.getNextAccept().isEmpty());

		//Minimize DFA
		MinimizeDFAAlgorithm c4 = new MinimizeDFAAlgorithm(dfa);
		c4.stepToCompletion();
		dfa = c4.getMinimizedDFA();
		outPrintln("Minimized regex DFA:\n" + dfa.toString());
		
		//convert DFA to rex 
		DFAtoRegularExpressionConverter c3 = new DFAtoRegularExpressionConverter(dfa);
		c3.stepToCompletion();
		regex = c3.getResultingRegEx();
		outPrintln("Regex from DFA:\n" + regex.toString());
		
	}

	@Override
	public String getTestName() {
		return "RegEx test";
	}

}
