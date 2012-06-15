package test;

import java.io.File;
import java.util.regex.Pattern;

import file.xml.XMLCodec;

import model.algorithms.conversion.autotogram.FSAtoRegGrammarConversion;
import model.algorithms.conversion.regextofa.RegularExpressionToNFAConversion;
import model.algorithms.fsa.DFAtoRegularExpressionConverter;
import model.algorithms.fsa.NFAtoDFAConverter;
import model.algorithms.fsa.minimizer.MinimizeDFAAlgorithm;
import model.algorithms.transform.grammar.CNFConverter;
import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.simulate.AutoSimulator;
import model.automata.simulate.AutomatonSimulator;
import model.automata.simulate.SingleInputSimulator;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.regex.RegularExpression;

public class RegExTest extends TestHarness{


	@Override
	public void runTest() {
		String toSave = System.getProperties().getProperty("user.dir") +"/filetest";
		File f = new File(toSave + "/regEx.jff");
		RegularExpression regex = (RegularExpression) new XMLCodec().decode(f);

		//set regex
		String in = "((a+b)*+c)";
		
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

		//convertToGrammar
		FSAtoRegGrammarConversion c1 = new FSAtoRegGrammarConversion(fsa);
		c1.stepToCompletion();
		Grammar g1 = c1.getConvertedGrammar();
		outPrintln("Grammar from regex: " + g1.toString());

		
		//convert grammar to CNF
		CNFConverter c25 = new CNFConverter(g1);
		c25.stepToCompletion();
		Grammar g2 = c25.getTransformedGrammar();
		outPrintln("Grammar to CNF: " + g2.toString());

		
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
