package view.automata;

import model.algorithms.testinput.simulate.AutomatonSimulator;
import model.algorithms.testinput.simulate.Configuration;
import model.automata.Automaton;
import model.automata.Transition;

public class SimulatorPanel<T extends Automaton<S>, S extends Transition<S>> extends AutomatonDisplayPanel<T, S> {

	private AutomatonSimulator mySimulator;
	private Configuration<T, S> myConfigs;

	public SimulatorPanel(AutomatonEditorPanel<T, S> editor, AutomatonSimulator simulator, Configuration<T, S> configs) {
		super(editor, "Simulate");
		mySimulator = simulator;
		myConfigs = configs;
		update();
	}

}
