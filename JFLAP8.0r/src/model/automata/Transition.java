package model.automata;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

import util.Copyable;
import util.GraphHelper;



import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

/**
 * A <CODE>Transition</CODE> object is a simple abstract class representing a
 * transition between two state objects in an automaton. Subclasses of this
 * transition class will have additional fields containing the particulars
 * necessary for their transition.
 * 
 * @see jflap.model.automaton.State
 * @see jflap.model.automaton.Automaton
 * 
 * @author Thomas Finley, Henry Qin
 */

public abstract class Transition implements LanguageFunction, Comparable<Transition>{

	/** The states this transition goes between. */
	private State myFrom;

	/** The states this transition goes between. */
	private State  myTo;

	/** 
	 * the string of symbols that allows some input
	 * to move along this transition
	 */
	private SymbolString myInput;

	private Point myControlPoint;

	/**
	 * Instantiates a new <CODE>Transition</CODE>.
	 * 
	 * @param from
	 *            the state this transition is from
	 * @param to
	 *            the state this transition moves to
	 * @param input
	 * 			  the input to move along this transition
	 */
	public Transition(State from, State to, SymbolString input) {
		this.myFrom = from;
		this.myTo = to;
		myControlPoint = GraphHelper.getCenterPoint(this);
		setInput(input);
	}

	public SymbolString getInput(){
		return myInput;
	}

	public double getCtrlX(){
		return myControlPoint.getX();
	}

	public double getCtrlY(){
		return myControlPoint.getY();
	}

	public void translateCtrlPt(int dx, int dy){
		myControlPoint.translate(dx, dy);
	}

	public void setInput(SymbolString input){
		myInput = input;
	}

	/**
	 * Returns a copy of this transition with the same <CODE>from</CODE> and
	 * <CODE>to</CODE> state.
	 * 
	 * @return a copy of this transition as described
	 */
	@Override
	public Transition clone(){
		try{
			Constructor cons = this.getClass().getConstructor(State.class, State.class, SymbolString.class);
			return (Transition) cons.newInstance(this.getFromState(), this.getToState(), this.getInput());
		}catch (Exception e){
			throw new AutomatonException(e);
		}
	}

	/**
	 * Returns the state this transition eminates from.
	 * 
	 * @return the state this transition eminates from
	 */
	public State getFromState() {
		return this.myFrom;
	}

	/**
	 * Returns the state this transition travels to.
	 * 
	 * @return the state this transition travels to
	 */
	public State getToState() {
		return this.myTo;
	}
	/**
	 * Sets the state the transition starts at.
	 * @param newFrom the state the transition starts at
	 */
	public void setFromState(State newFrom) {
		this.myFrom = newFrom;
	}

	/**
	 * Sets the state the transition goes to.
	 * @param newTo the state the transition goes to
	 */
	public void setToState(State newTo) {
		this.myTo = newTo;
	}


	/**
	 * Checks if this transition is a loop, i.e. if
	 * its from state is the same as its to state
	 * @return
	 */
	public boolean isLoop() {
		return this.myTo.equals(myFrom);
	}
	
	public abstract String getLabelText();
	
	/**
	 * Checks to see if this transition has a lambda input
	 * component. subclasses should override this method
	 * if the definition of a lambda transition is different,
	 * specifically in the case of closure.
	 * 
	 * @return
	 */
	public boolean isLambdaTransition() {
		return this.getInput().isEmpty();
	}

	/**
	 * Returns a string representation of this object. The string returned is
	 * the string representation of the first state, and the string
	 * representation of the second state.
	 * 
	 * @return a string representation of this object
	 */
	@Override
	public String toString() {
		return this.getFromState().getName() + "---" + 
								this.getLabelText() + "--->" + 
									this.getToState().getName();
	}

	/**
	 * Returns if this transition equals another object.
	 * 
	 * @param object
	 *            the object to test against
	 * @return <CODE>true</CODE> if the two are equal, <CODE>false</CODE>
	 *         otherwise
	 */
	@Override
	public boolean equals(Object o) {
		return this.compareTo((Transition) o) == 0;
	}

	/**
	 * Returns the hash code for this transition.
	 * 
	 * @return the hash code for this transition
	 */
	@Override
	public int hashCode() {
		return myFrom.hashCode() ^ myTo.hashCode();
	}

	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		return getInput().getUniqueSymbolsUsed();
	}

	@Override
	public boolean purgeOfSymbol(Symbol s) {
		return getInput().purgeOfSymbol(s);
	}

	@Override
	public int compareTo(Transition o) {
		int compare = this.getFromState().compareTo(o.getFromState());
		if (compare == 0)
			compare = this.getToState().compareTo(o.getToState());
		if (compare == 0)
			compare = this.getInput().compareTo(o.getInput());
		return compare;
	}

}