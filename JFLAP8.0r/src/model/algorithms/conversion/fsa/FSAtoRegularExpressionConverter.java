package model.algorithms.conversion.fsa;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import debug.JFLAPDebug;

import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.SteppableAlgorithm;
import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FSTransition;
import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.regex.GeneralizedTransitionGraph;
import model.regex.OperatorAlphabet;
import model.regex.RegularExpression;

public class FSAtoRegularExpressionConverter extends FormalDefinitionAlgorithm<FiniteStateAcceptor> {



	private GeneralizedTransitionGraph myGTG;
	private RegularExpression myRegEx;
	private List<FSTransition> newLambdaTransitions;
	private List<FSTransition> myCollapseList;
	private List<FSTransition> myEmptyTransitionsNeeded;
	public List<State> myStatesToCollapse;


	public FSAtoRegularExpressionConverter (FiniteStateAcceptor fsa){
		super(fsa);
	}

	@Override
	public String getDescriptionName() {
		return "Finite Accepter to Regular Expression Converter";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		
		return new AlgorithmStep[]{new DoSingleFinalState(),
				new CollapseTransitions(),
				new CreateEmptyTransitions(),
				new CollapseStates()};
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myRegEx = new RegularExpression((InputAlphabet) getFA().getInputAlphabet().copy());
		myGTG = new GeneralizedTransitionGraph(getFA());
		newLambdaTransitions = new ArrayList<FSTransition>();
		myCollapseList = getTransitionCollapseList();
		myEmptyTransitionsNeeded = getEmptyTransitionsNeeded();
		myStatesToCollapse = getStatesToCollapse();
		return true;
	}


	@Override
	public BooleanWrapper[] checkOfProperForm(FiniteStateAcceptor fd) {
		State s = fd.getStartState();
		boolean startIsFinal = fd.getFinalStateSet().contains(s);
		BooleanWrapper bw = new BooleanWrapper(!startIsFinal, "The start state of the FA must not " +
				"also be a final state when converting to a regular expression.");
		if (bw.isFalse())
			return new BooleanWrapper[]{bw};
		return new BooleanWrapper[0];
	}

	public RegularExpression getResultingRegEx(){
		if (this.isRunning())
			throw new AlgorithmException("You may not retrieve the regular expression " +
					"until the GTG is fully simplified");
		
		if (myRegEx.isComplete().length == 0) return myRegEx;
		
		State start = getGTG().getStartState();
		FinalStateSet finalSet = getGTG().getFinalStateSet();
		
		State end = finalSet.first();
		
		TransitionSet<FSTransition> transitions = getGTG().getTransitions();
		
		FSTransition t1 = transitions.getTransitionsFromStateToState(start, start).iterator().next();
		FSTransition t2 = transitions.getTransitionsFromStateToState(start, end).iterator().next();
		FSTransition t3 = transitions.getTransitionsFromStateToState(end, end).iterator().next();
		
		SymbolString exp = new SymbolString(t2.getInput());
		if (!isEmptySetTransition(t1)){
			exp.addAll(0,star(t1.getInput()));
		}
		
		if (!isEmptySetTransition(t3)){
			exp.addAll(star(t3.getInput()));
		}
		myRegEx.setTo(exp);
		
		return myRegEx;

	}
	
	private SymbolString star(SymbolString input) {
		OperatorAlphabet alph = myRegEx.getOperators();
		Symbol open = alph.getOpenGroup();
		Symbol close = alph.getCloseGroup();
		if (input.size() > 1 && !(input.startsWith(open) && 
				input.endsWith(close))){
			input.addFirst(alph.getOpenGroup());
			input.addLast(alph.getCloseGroup());
		}
		input.add(alph.getKleeneStar());
		
		return input;
	}

	/**
	 * Creates a single final state and sets up all of lamba transitions that
	 * must be added from the old final states. DOES NOT remove final states
	 * from final state set.
	 * @return
	 */
	public boolean createSingleFinalState() {

		State newFinal = myGTG.getStates().createAndAddState();
		FinalStateSet finalStates = myGTG.getFinalStateSet();
		for(State s: finalStates){
			newLambdaTransitions.add(new FSTransition(s, newFinal));
		}

		return finalStates.add(newFinal);
	}

	private boolean hasSingleFinal() {
		return getGTG().getFinalStateSet().size() == 1;
	}

	private FiniteStateAcceptor getFA() {
		return getOriginalDefinition();
	}

	private List<FSTransition> getTransitionCollapseList() {
		StateSet states = getGTG().getStates();
		ArrayList<FSTransition> collapseTo = new ArrayList<FSTransition>();
		TransitionSet<FSTransition> trans = getGTG().getTransitions();
		for (State s1: states){
			for(State s2: states){
				Set<FSTransition> fromTo = trans.getTransitionsFromStateToState(s1, s2);
				if (fromTo.size() > 1){
					collapseTo.add(fromTo.iterator().next());
				}
			}
		}
		return collapseTo;
	}

	private List<FSTransition> getEmptyTransitionsNeeded() {
		StateSet states = getGTG().getStates();
		ArrayList<FSTransition> toAdd = new ArrayList<FSTransition>();
		TransitionSet<FSTransition> transSet = getGTG().getTransitions();
		for (State from: states){
			for(State to: states){
				if(transSet.getTransitionsFromStateToState(from, to).isEmpty())
					toAdd.add(new FSTransition(from, 
							to, 
							new SymbolString(EMPTY_SET_SYMBOL)));
			}
		}
		return toAdd;
	}

	private boolean addAllEmptyTransitions(){
		if (myEmptyTransitionsNeeded.isEmpty()) return false;
		boolean added = getGTG().getTransitions().addAll(myEmptyTransitionsNeeded);
		if (added)
			myEmptyTransitionsNeeded.clear();
		return added;	
	}

	public FSTransition addEmptyTransition(State from, State to){
		for (FSTransition t: myEmptyTransitionsNeeded){
			if (t.getToState().equals(to) && t.getFromState().equals(from)){
				getGTG().getTransitions().add(t);
				myEmptyTransitionsNeeded.remove(t);
				return t;
			}
		}
		return null;
	}


	public GeneralizedTransitionGraph getGTG() {
		return myGTG;
	}

	public boolean collapseAllStates() {
		for (State s: new ArrayList<State>(myStatesToCollapse)){
			boolean collapsed = collapseState(s);
			if (!collapsed)
				return false;
		}
		return true;
	}

	public boolean collapseState(State s) {
		if (!myStatesToCollapse.contains(s))
			return false;

		myStatesToCollapse.remove(s);
		Collection<FSTransition> toAdd = getTransitionsForCollapseState(s);
		return getGTG().getStates().remove(s) && 
				getGTG().getTransitions().addAll(toAdd);
		
	}

	public Collection<FSTransition> getTransitionsForCollapseState(State k) {
		ArrayList<FSTransition> list = new ArrayList<FSTransition>();
		StateSet states = getGTG().getStates();
		for (State p : states) {
			
			if(!validTransitionForCollapse(p,k))
				continue;
			
			for (State q: states) {
				
				if (!validTransitionForCollapse(k,q))
					continue;
				
				SymbolString exp = getExpression(p, q, k);
				list.add(new FSTransition(p, q, exp));
			}
		}
		return list;
	}

	private boolean validTransitionForCollapse(State from, State to) {
		if (from.equals(to))
			return false;
		
		TransitionSet<FSTransition> transitions = getGTG().getTransitions();
		Set<FSTransition> pk = 
				transitions.getTransitionsFromStateToState(from, to);
		
		FSTransition test = (FSTransition) pk.toArray()[0];

		if (isEmptySetTransition(test))
			return false;
		
		return true;
		
	}

	private boolean isEmptySetTransition(FSTransition test) {
		return isEmpty(test.getInput());
	}

	private boolean isEmpty(SymbolString input) {
		return input.contains(EMPTY_SET_SYMBOL);
	}

	/**
	 * Returns the expression obtained from evaluating the following equation:
	 * r(pq) = r(pq) + r(pk)r(kk)*r(kq), where p, q, and k represent the IDs of
	 * states in <CODE>automaton</CODE>.
	 * 
	 * @param p
	 *            the from state
	 * @param q
	 *            the to state
	 * @param k
	 *            the state being removed.
	 * @param automaton
	 *            the automaton.
	 * @return the expression obtained from evaluating the following equation:
	 *         r(pq) = r(pq) + r(pk)r(kk)*r(kq), where p, q, and k represent the
	 *         IDs of states in <CODE>automaton</CODE>.
	 */
	private SymbolString getExpression(State p, State q, State k) {

		SymbolString pq = getExpressionBetweenStates(p, q),
				pk = getExpressionBetweenStates(p, k),
				kk = getExpressionBetweenStates(k, k),
				kq = getExpressionBetweenStates(k, q);


		OperatorAlphabet ops = myRegEx.getOperators();

		kk = star(kk);
		pk.addAll(kk);
		pk.addAll(kq);
		if (!isEmpty(pq)){
			pk.add(ops.getUnionOperator());
			pk.addAll(pq);
		}
		return pk;
	}

	/**
	 * Returns the expression on the transition between <CODE>fromState</CODE>
	 * and <CODE>toState</CODE> in <CODE>automaton</CODE>.
	 * 
	 * @param fromState
	 *            the from state
	 * @param toState
	 *            the to state
	 * @param automaton
	 *            the automaton
	 * @return the expression on the transition between <CODE>fromState</CODE>
	 *         and <CODE>toState</CODE> in <CODE>automaton</CODE>.
	 */
	private SymbolString getExpressionBetweenStates(State fromState, State toState) {
		Set<FSTransition> transitions = getGTG().getTransitions()
				.getTransitionsFromStateToState(fromState, toState);
		FSTransition trans = transitions.toArray(new FSTransition[0])[0];
		return new SymbolString(trans.getInput());
	}

	private List<State> getStatesToCollapse() {
		List<State> toCollapse = new ArrayList<State>();
		toCollapse.addAll(getGTG().getStates());
		toCollapse.remove(getGTG().getStartState());
		if (hasSingleFinal()){
			toCollapse.removeAll(getGTG().getFinalStateSet());
		}
		return toCollapse;
	}

	private boolean addAllTransitionsToFinal() {
		boolean added = this.getGTG().getTransitions().addAll(newLambdaTransitions);

		if (added){
			for (FSTransition t: newLambdaTransitions){
				this.getGTG().getFinalStateSet().remove(t.getFromState());
			}
			newLambdaTransitions.clear();
		}

		return added;
	}



	private boolean collapseAllTransitions() {
		if (myCollapseList.isEmpty()) return false;
		for (FSTransition trans : myCollapseList.toArray(new FSTransition[0]) ){
			this.collapseTransitionsOn(trans);
		}
		return true;
	}

	public boolean collapseTransitionsOn(FSTransition trans) {

		if (!shouldCollapse(trans)){
			return false;
		}

		removeCollapse(trans);

		TransitionSet<FSTransition> transSet = getGTG().getTransitions();
		Set<FSTransition> fromTo = 
				transSet.getTransitionsFromStateToState(trans.getFromState(), 
						trans.getToState());

		transSet.removeAll(fromTo);

		SymbolString regexLabel = new SymbolString();
		Symbol union = myRegEx.getOperators().getUnionOperator();
		for (FSTransition t: fromTo){
			if (isEmptySetTransition(t)) 
				continue;
			regexLabel.add(union);
			
			if (isLambdaTransition(t))
				regexLabel.add(myRegEx.getOperators().getEmptySub());
			regexLabel.addAll(t.getInput());
		}

		regexLabel = regexLabel.subList(1);
		regexLabel.addFirst(myRegEx.getOperators().getOpenGroup());
		regexLabel.add(myRegEx.getOperators().getCloseGroup());

		FSTransition collapsed = new FSTransition(trans.getFromState(), 
				trans.getToState(),
				regexLabel);

		return transSet.add(collapsed);
	}

	private boolean isLambdaTransition(FSTransition t) {
		return t.getInput().isEmpty();
	}

	private void removeCollapse(FSTransition trans) {
		myCollapseList.remove(getCollapseTransition(trans));
	}

	private FSTransition getCollapseTransition(FSTransition trans) {
		for (FSTransition t: myCollapseList){
			if (t.getToState().equals(trans.getToState()) &&
					t.getFromState().equals(trans.getFromState())){
				return t;
			}
		}
		return null;
	}

	private boolean shouldCollapse(FSTransition trans) {
		return getCollapseTransition(trans) != null;
	}

	private class DoSingleFinalState implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Make Single Final State";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {

			if (getGTG().getFinalStateSet().size() > 1)
				createSingleFinalState();

			return addAllTransitionsToFinal();
		}

		@Override
		public boolean isComplete() {
			return hasSingleFinal();
		}

	}

	private class CollapseTransitions implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Collapse all transitions.";
		}

		@Override
		public String getDescription() {
			return "Transform multiple transitions with the same " +
					"from and to states into a single regular expression " +
					"transition.";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return collapseAllTransitions();
		}

		@Override
		public boolean isComplete() {
			return myCollapseList.isEmpty();
		}

	}

	private class CreateEmptyTransitions implements AlgorithmStep{


		@Override
		public String getDescriptionName() {
			return "Create empty set transitions.";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return addAllEmptyTransitions();
		}

		@Override
		public boolean isComplete() {
			return myEmptyTransitionsNeeded.isEmpty();
		}

	}

	private class CollapseStates implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Collapse all states";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return collapseAllStates();
		}

		@Override
		public boolean isComplete() {
			return myStatesToCollapse.isEmpty();
		}

	}

}
