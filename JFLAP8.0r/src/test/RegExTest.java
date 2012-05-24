package test;

import java.util.regex.Pattern;

import model.algorithms.conversion.regextofa.RegularExpressionToNFAConversion;
import model.automata.InputAlphabet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.formaldef.components.symbols.Symbol;
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
	}

}
