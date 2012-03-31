package model.automata.algorithms;

import model.algorithms.SteppableAlgorithm;
import model.automata.Automaton;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.Terminal;
import model.grammar.Grammar;
import errors.BooleanWrapper;

public abstract class AutomatonToGrammarConversion<T extends Automaton> implements SteppableAlgorithm {

	private Grammar myConvertedGrammar;
	
	private T myAutomaton;
	
	public AutomatonToGrammarConversion(T automaton) {
		myAutomaton = automaton;
		myConvertedGrammar = new Grammar();
		
		for (Symbol s: myAutomaton.getInputAlphabet()){
			myConvertedGrammar.getTerminals().add(new Terminal(s.toString()));
		}
		
		
	}

}
