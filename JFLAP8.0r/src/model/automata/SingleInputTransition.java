package model.automata;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.Set;
import java.util.TreeSet;

import util.Copyable;



import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.graph.GraphHelper;

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

public abstract class SingleInputTransition<T extends SingleInputTransition<T>> extends Transition<T>{

	/** 
	 * the string of symbols that allows some input
	 * to move along this transition
	 */
	private SymbolString myInput;


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
	public SingleInputTransition(State from, State to, SymbolString input) {
		super(from, to);
		myInput = input;
	}

	public SymbolString getInput(){
		return myInput;
	}


	public boolean setInput(SymbolString input){
		return myInput.setTo(input);
	}

	@Override
	public SymbolString[] getPartsForAlphabet(Alphabet a) {
		if (a instanceof InputAlphabet)
			return new SymbolString[]{this.getInput()};
		return new SymbolString[0];
	}
	
	@Override
	public int compareTo(T o) {
		int compare = this.getFromState().compareTo(o.getFromState());
		if (compare == 0)
			compare = this.getToState().compareTo(o.getToState());
		if (compare == 0)
			compare = this.getInput().compareTo(o.getInput());
		return compare;
	}

}