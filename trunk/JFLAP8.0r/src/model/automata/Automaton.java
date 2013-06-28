package model.automata;

import java.io.ObjectInputStream.GetField;
import java.util.Collection;
import java.util.Set;

import javax.sql.rowset.spi.TransactionalWriter;

import debug.JFLAPDebug;

import model.change.events.AdvancedChangeEvent;
import model.formaldef.FormalDefinition;
import model.formaldef.FormalDefinitionException;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;

public abstract class Automaton<T extends Transition<T>> extends FormalDefinition{

	private StartState myStartState;

	public Automaton(FormalDefinitionComponent ... comps) {
		super(comps); 
		myStartState = getComponentOfClass(StartState.class);
		if (getStartState() != null)
			this.getStates().add(getStartState());
	}

	public InputAlphabet getInputAlphabet(){
		return getComponentOfClass(InputAlphabet.class);
	}

	public TransitionSet<T> getTransitions(){
		return getComponentOfClass(TransitionSet.class);
	}

	public State getStartState() {
		return getComponentOfClass(StartState.class).getState();
	}

	public void setStartState(State s){
		getComponentOfClass(StartState.class).setState(s);
	}


	public StateSet getStates() {
		return getComponentOfClass(StateSet.class);
	}

	@Override
	public void componentChanged(AdvancedChangeEvent event) {
		if (event.comesFrom(this.getStates()) && event.getType() == ITEM_REMOVED){
			TransitionSet<T> transSet = this.getTransitions();
			Collection<State> s = (Collection<State>) event.getArg(0);
			transSet.removeForStates(s);
			myStartState.checkAndRemove(s);
		}
		else if(event.comesFrom(StartState.class) && event.getType() == SPECIAL_CHANGED){
			State s = this.getStartState();
			if (s != null)
				this.getStates().add(s);
		}
		else if (event.comesFrom(TransitionSet.class) && 
				event.getType() == ITEM_ADDED){
			TransitionSet<T> transSet = (TransitionSet<T>) event.getSource();
			Set<State> used = transSet.getAllStatesUsed();
			this.getStates().addAll(used);
		}
		super.componentChanged(event);
		distributeChange(event);
	}

	public T createAndAddTransiton(State from, State to){
		StateSet states = this.getStates();
		TransitionSet<T> transitions = this.getTransitions();
		
		if (!(states.contains(from)&&states.contains(to)))
			return null;
		T newTrans = createBlankTransition(from, to);
		if(!transitions.add(newTrans))
			return null;
		return newTrans;
	}
	
	public abstract T createBlankTransition(State from, State to);

	@Override
	public InputAlphabet getLanguageAlphabet() {
		return getInputAlphabet();
	}

	public static boolean isStartState(Automaton a, State s){
		return s.equals(a.getStartState());
	}
}
