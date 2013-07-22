package model.algorithms.testinput.simulate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.automata.turing.TuringMachine;
import model.symbols.SymbolString;

public class SingleInputSimulator extends AutomatonSimulator {

	private LinkedHashSet<ConfigurationChain> myChains;
	private int mySpecialCase;
	private Configuration myInitialConfiguration;
	private boolean closure;

	public static final int DEFAULT = 0, ACCEPT_BY_EMPTY_STACK = 1;

	public SingleInputSimulator(Automaton a, int specialCase, boolean closure) {
		super(a);
		myChains = new LinkedHashSet<ConfigurationChain>();
		mySpecialCase = specialCase;
		this.closure = closure;
	}

	public SingleInputSimulator(Automaton a, boolean closure) {
		this(a, DEFAULT, closure);
	}

	public boolean canStep() {
		for (ConfigurationChain chain : myChains) {
			if (!chain.isHalted())
				return true;
		}
		return false;
	}

	public ConfigurationChain[] step() {

		LinkedList<ConfigurationChain> copy = new LinkedList<ConfigurationChain>(
				myChains);
		myChains.clear();
		for (ConfigurationChain chain : copy) {
			if (chain.isFrozen())
				myChains.add(chain);
			else if (chain.isFinished()) {
				// myChains.remove(chain);
				// As myChains is clear, don't do anything, just ignore it
			} else {
				myChains.addAll(stepAndFork(chain));
			}
		}

		updateSelectedStates();
		// JFLAPDebug.print(myChains);

		return myChains.toArray(new ConfigurationChain[0]);
	}

	public Collection<? extends ConfigurationChain> stepAndFork(
			ConfigurationChain chain) {
		ArrayList<ConfigurationChain> chains = new ArrayList<ConfigurationChain>();

		LinkedList<Configuration> nextConfigs = chain.getCurrentConfiguration()
				.getNextConfigurations();

		// Configuration next = nextConfigs.pollFirst();
		// chain.add(next);
		// chains.add(chain);
		if (closure) {
			for (Configuration c : nextConfigs) {
				if (!c.getTransitionTo().isLambdaTransition()) {
					String nextID = chain.getID() + chain.getNumChildren();
					ConfigurationChain newChain = new ConfigurationChain(c,
							chain.clone(), nextID);
					chains.add(newChain);
					
					Set<State> seen = new HashSet<State>();
					seen.add(c.getState());
					addClosure(newChain, c, chains, seen);
					chain.incrementNumChildren();
				}
			}
		} else
			for (Configuration c : nextConfigs) {
				String nextID = chain.getID() + chain.getNumChildren();
				chains.add(new ConfigurationChain(c, chain.clone(), nextID));
				chain.incrementNumChildren();
			}

		return chains;
	}

	private List<ConfigurationChain> getAllAcceptChains() {
		List<ConfigurationChain> toReturn = new ArrayList<ConfigurationChain>();
		for (ConfigurationChain chain : myChains) {
			if (chain.isAccept())
				toReturn.add(chain);
		}
		return toReturn;
	}

	public void updateSelectedStates() {
		// this.getAutomaton().clearSelection();
		// for (ConfigurationChain chain : myChains){
		// if (!chain.isFrozen())
		// chain.getCurrentConfiguration().getState().setSelected(true);
		// }
	}

	public ConfigurationChain[] reverse() {
		Set<ConfigurationChain> toRemove = new HashSet<ConfigurationChain>();
		Set<ConfigurationChain> toAdd = new HashSet<ConfigurationChain>();
		for (ConfigurationChain chain : myChains) {
			if (chain.isFrozen())
				continue;
			chain.reverse();
			if (chain.isEmpty()) {
				// Fix reverse
				toAdd.add(chain.getParent());
				toRemove.add(chain);
			}
		}

		myChains.removeAll(toRemove);
		myChains.addAll(toAdd);
		updateSelectedStates();
		return myChains.toArray(new ConfigurationChain[0]);
	}

	@Override
	public String getDescriptionName() {
		return "Simulate input on " + this.getAutomaton().getDescriptionName();
	}

	public void clear() {
		myChains.clear();
		myInitialConfiguration = null;
		this.updateSelectedStates();
	}

	@Override
	public void beginSimulation(SymbolString... input) {
		Configuration init = createInitConfig(input);
		beginSimulation(init);
	}

	private Configuration createInitConfig(SymbolString... input) {
		return createInitConfig(getAutomaton().getStartState(), input);
	}

	private Configuration createInitConfig(State s, SymbolString... input) {
		return ConfigurationFactory.createInitialConfiguration(
				this.getAutomaton(), s, input);
	}

	public void beginSimulation(Configuration c) {
		this.clear();
		myInitialConfiguration = c;
		ConfigurationChain chain = new ConfigurationChain(
				myInitialConfiguration, null, "0");
		myChains.add(chain);
		if (closure) {
			Set<State> seen = new HashSet<State>();
			seen.add(c.getState());
			addClosure(chain, myInitialConfiguration, myChains, seen);
		}
		this.updateSelectedStates();
	}

	public void removeConfigurationChain(ConfigurationChain chain) {
		myChains.remove(chain);
		updateSelectedStates();
	}

	public LinkedHashSet<ConfigurationChain> getChains() {
		return myChains;
	}

	public void freezeAll() {
		for (ConfigurationChain chain : getChains()) {
			chain.freeze();
		}
		updateSelectedStates();
	}

	public void thawAll() {
		for (ConfigurationChain chain : getChains()) {
			chain.thaw();
		}
		updateSelectedStates();
	}

	public boolean canReverse() {
		for (ConfigurationChain chain : this.getChains()) {
			if (!chain.isFrozen()
					&& (chain.size() > 1 || chain.getParent() != null))
				return true;
		}
		return false;
	}

	public boolean isRunning() {
		return myInitialConfiguration != null;
	}

	public int getSpecialAcceptCase() {
		return mySpecialCase;
	}

	public void reset() {
		beginSimulation(myInitialConfiguration);
	}

	public void freezeConfigurationChain(ConfigurationChain chain) {
		chain.freeze();
		this.updateSelectedStates();
	}

	public void thawConfigurationChain(ConfigurationChain chain) {
		chain.thaw();
		this.updateSelectedStates();
	}

	@Override
	public SingleInputSimulator copy() {
		return new SingleInputSimulator(getAutomaton(), getSpecialAcceptCase(),
				closure);
	}

	@Override
	public String getDescription() {
		return null;
	}

	private void addClosure(ConfigurationChain chain, Configuration current,
			Collection<ConfigurationChain> chains, Set<State> seen) {
		Automaton auto = getAutomaton();
		TransitionSet transitions = auto.getTransitions();
		Set<Transition> to = transitions.getTransitionsFromState(current.getState());
		
		int numClosure = 0;
		LinkedList<Configuration> next = current.getNextConfigurations();
	
		for (Configuration config : next) {
			Transition trans = config.getTransitionTo();
			
			if (trans.isLambdaTransition()) {
				State s = config.getState();
				
				if(!trans.isLoop() && !seen.contains(s)){
					String nextID = chain.getID() + chain.getNumChildren();
					ConfigurationChain newChain = new ConfigurationChain(config, chain.clone(), nextID);
					
					chain.incrementNumChildren();
					chains.add(newChain);
					
					seen.add(s);
					addClosure(newChain, config, chains, seen);
					seen.remove(s);
				}
				numClosure++;
			}
		}
//		if (numClosure != 0 && numClosure == to.size() && !chain.isHalted())
//			chains.remove(chain);
	}
}
