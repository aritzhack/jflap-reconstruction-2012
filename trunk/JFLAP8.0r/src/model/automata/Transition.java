package model.automata;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;



import model.formaldef.components.functionset.function.LanguageFunction;

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

public abstract class Transition<T extends TransitionLabel> implements LanguageFunction, 
																		Comparable<Transition<T>>{

	/** The states this transition goes between. */
	private State myFrom;

	/** The states this transition goes between. */
	private State  myTo;

	private T myLabel;

	/**
	 * Instantiates a new <CODE>Transition</CODE>.
	 * 
	 * @param from
	 *            the state this transition is from
	 * @param to
	 *            the state this transition moves to
	 */
	public Transition(State from, State to, T label) {
		this.myFrom = from;
		this.myTo = to;
		this.setLabel(label);
	}

	public Transition(State from, State to){
		myFrom = from;
		myTo = to;
		this.setLabel(createEmptyLabel()); 
	}

	private T createEmptyLabel() {
		try {
			return (T) this.getLabelClass().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	private Class<?> getLabelClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * Returns a copy of this transition with the same <CODE>from</CODE> and
	 * <CODE>to</CODE> state.
	 * 
	 * @return a copy of this transition as described
	 */
	@Override
	public Transition<T> clone() {
		try{
			Constructor constr = this.getClass().getConstructor(State.class, 
					State.class,
					this.getLabelClass());
			return (Transition<T>) constr.newInstance(getFromState(), getToState(), this.getLabel().clone());
		} catch (Exception e){
			throw new RuntimeException(e);
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
	 * Returns the label for this transition.
	 */
	public T getLabel() {
		return myLabel;
	}

	/**
	 * Sets the label for this transition.
	 * 
	 * @param myLabel2
	 *            the new label for this transition
	 * @throws IllegalArgumentException
	 *             if the label contains any "bad" characters, i.e., not
	 *             alphanumeric
	 */
	public void setLabel(T label) {
		myLabel = label;
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
		return "[" + getFromState().toString() + "] -> ["
				+ getToState().toString() + "]" + ": \"" + getLabel() + "\"";
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
	public boolean equals(Object object) {
		try {
			Transition t = (Transition) object;
			return myFrom.equals(t.getFromState()) && myTo.equals(t.getToState()) && this.getLabel().equals(t.getLabel());
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * Returns the hash code for this transition.
	 * 
	 * @return the hash code for this transition
	 */
	@Override
	public int hashCode() {
		return myFrom.hashCode() ^ myTo.hashCode() ^ this.getLabel().hashCode();
	}

	public boolean isLoop() {
		return this.myTo.equals(myFrom);
	}


	@Override
	public int compareTo(Transition<T> o) {
		return this.getLabel().compareTo(o.getLabel());
	}

}