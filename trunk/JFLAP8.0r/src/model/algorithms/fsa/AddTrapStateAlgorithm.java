package model.algorithms.fsa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.algorithms.FormalDefinitionAlgorithm;
import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.acceptors.fsa.FSTransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.derterminism.DeterminismChecker;
import model.automata.derterminism.FSADeterminismChecker;
import model.formaldef.FormalDefinition;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class AddTrapStateAlgorithm extends FormalDefinitionAlgorithm<FiniteStateAcceptor> {

	private static final String TRAP = "TRAP";
	private FiniteStateAcceptor myNewDFA;
	private State myTrapState;
	private Set<FSTransition> myTransitionsNeeded;

	public AddTrapStateAlgorithm(FiniteStateAcceptor fd) {
		super(fd);
	}

	@Override
	public String getDescriptionName() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(FiniteStateAcceptor dfa) {
		List<BooleanWrapper> errors = new ArrayList<BooleanWrapper>();
		FSADeterminismChecker check = new FSADeterminismChecker();
		if (!check.isDeterministic(dfa))
			errors.add(new BooleanWrapper(false,"You may not add a trap state to an NFA"));
		if(!FiniteStateAcceptor.hasAllSingleSymbolInput(dfa))
			errors.add(new BooleanWrapper(false, "The DFA to convert must have transitions " +
					"with either 1 or 0 input symbols."));
		return errors.toArray(new BooleanWrapper[0]);
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{
				new AddStateStep(),
				new AddTransitionsStep()
		};
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myNewDFA = (FiniteStateAcceptor) this.getDFA().copy();
		myTrapState = null;
		myTransitionsNeeded = null;
		return true;
	}

	private FiniteStateAcceptor getDFA() {
		return super.getOriginalDefinition();
	}
	
	private boolean AutoAddState() {
		State s = myNewDFA.getStates().createAndAddState();
		setupState(s);
		return true;
	}

	public boolean addStateForTrapState(State s) {
		if (!myNewDFA.getStates().add(s))
			return false;
		setupState(s);
		return true;
	}
	
	public boolean addTransition(State from, Symbol s){
		for (FSTransition trans: myTransitionsNeeded.toArray(new FSTransition[0])){
			if (trans.getFromState().equals(from) &&
					trans.getInput().startsWith(s)){
				if (!addTransition(trans))
					return false;
			}
		}
		return true;
	}

	public FiniteStateAcceptor getDFAWithTrapState() {
		return myNewDFA;
	}
	

	public boolean trapStateNeeded() {
		return !getAllTransitionsNeeded().isEmpty();
	}

	private boolean addTransition(FSTransition trans) {
		if (!myNewDFA.getTransitions().add(trans))
			return false;
		return myTransitionsNeeded.remove(trans);
	}

	private void setupState(State s) {
		s.setLabel(TRAP);
		s.setName("TRAPPPPPPP");
		myTrapState = s;
		myTransitionsNeeded = getAllTransitionsNeeded();
	}

	private Set<FSTransition> getAllTransitionsNeeded() {
		Set<FSTransition> toAdd = new TreeSet<FSTransition>();
		for (State s: myNewDFA.getStates()){
			toAdd.addAll(getTransitionsNeededFor(s));
		}
		return toAdd;
	}

	private Collection<FSTransition> getTransitionsNeededFor(State from) {
		Collection<FSTransition> trans = myNewDFA.getTransitions().getTransitionsFromState(from);
		
		Set<FSTransition> needed = new TreeSet<FSTransition>();
		
		for (Symbol s: myNewDFA.getInputAlphabet()){
			boolean exists = false;
			for (FSTransition t: trans){
				if (t.getInput().startsWith(s)){
					exists = true;
					break;
				}
			}
			if (!exists)
				needed.add(new FSTransition(from, myTrapState, new SymbolString(s)));
		}
		
		return needed;
	}

	private boolean addAllTransitionsNeeded() {
		for (FSTransition trans: myTransitionsNeeded.toArray(new FSTransition[0])){
			if (!addTransition(trans))
				return false;
		}
		return true;
	}

	
	public static boolean trapStateNeeded(FiniteStateAcceptor fsa) {
		FSADeterminismChecker check = new FSADeterminismChecker();
		if (!check.isDeterministic(fsa))
			return false;
		
		int transNum = fsa.getInputAlphabet().size();
		for (State s: fsa.getStates()){
			Set<FSTransition> fromSet = fsa.getTransitions().getTransitionsFromState(s);
			if (fromSet.size() != transNum)
				return true;
		}
		return false;
	}


	private class AddStateStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Add Trap State";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return AutoAddState();
		}

		@Override
		public boolean isComplete() {
			return myTrapState != null;
		}
		
	}

	private class AddTransitionsStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Add Transitions to Trap State";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return addAllTransitionsNeeded();
		}

		@Override
		public boolean isComplete() {
			return myTransitionsNeeded.isEmpty();
		}
		
	}

}