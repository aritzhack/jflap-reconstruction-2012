package model.automata.simulate;

import universe.preferences.JFLAPPreferences;
import model.automata.Automaton;
import model.automata.State;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.simulate.configurations.FSAConfiguration;
import model.automata.simulate.configurations.MealyConfiguration;
import model.automata.simulate.configurations.MooreConfiguration;
import model.automata.simulate.configurations.PDAConfiguration;
import model.automata.simulate.configurations.TMConfiguration;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.moore.MooreMachine;
import model.automata.turing.TuringMachine;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;



public class ConfigurationFactory {

	private static final Class<?>[] CHAIN_TYPES = new Class<?>[]{FSAConfiguration.class,
		MealyConfiguration.class,
		MooreConfiguration.class,
		TMConfiguration.class,
		PDAConfiguration.class};

	private static final int NUM_BLANKS = 40;

	public static Configuration createInitialConfiguration(Automaton a, SymbolString ... input) {
		State s = a.getStartState();
		if (a instanceof FiniteStateAcceptor){
			return new FSAConfiguration((FiniteStateAcceptor) a,s, 0, input[0]);
		}
		else if(a instanceof MooreMachine){
			return new MooreConfiguration((MooreMachine) a, s, 0, input[0], new SymbolString());
		}
		else if(a instanceof MealyMachine){
			return new MealyConfiguration((MealyMachine) a, s, 0, input[0], new SymbolString());
		}
		else if(a instanceof TuringMachine){
			return new TMConfiguration((TuringMachine) a, 
					s, 
					createTMPosArray((TuringMachine) a), 
					createTMOutput((TuringMachine) a, input));
		}
		else if(a instanceof PushdownAutomaton){
			Symbol bos = ((PushdownAutomaton) a).getBottomOfStackSymbol();
			return new PDAConfiguration((PushdownAutomaton) a, s, 0, input[0], new SymbolString(bos));
		}
		else 
			return null;


	}


	private static int[] createTMPosArray(TuringMachine a) {
		int[] positions = new int[a.getNumTapes()];
		for (int i = 0; i < positions.length; i++){
			positions[i] = NUM_BLANKS;
		}
		return positions;
	}


	private static SymbolString[] createTMOutput(TuringMachine a,
			SymbolString[] input) {
		SymbolString[] tapes = new SymbolString[input.length];
		for (int n = 0; n < tapes.length; n++){
			SymbolString tape = new SymbolString();
			tape.concat(createBlankString());
			tape.concat(input[n]);
			tape.concat(createBlankString());

			tapes[n] = tape;
		}
		return tapes;
	}


	private static SymbolString createBlankString() {
		SymbolString tape = new SymbolString();
		for (int n = 0; n < NUM_BLANKS; n++){
			tape.addLast(new Symbol("" + JFLAPPreferences.getTMBlankSymbol()));
		}
		return tape;
	}

}
