package model.automata;

import java.awt.FontFormatException;
import java.util.Set;

import util.UtilFunctions;

import debug.JFLAPDebug;

import model.change.ChangeEvent;
import model.change.events.RemoveEvent;
import model.change.events.RemoveStateEvent;
import model.formaldef.FormalDefinition;
import model.formaldef.FormalDefinitionException;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.symbols.Symbol;
import model.undo.IUndoRedo;

public abstract class Automaton<T extends Transition> extends FormalDefinition{

	private boolean amRemovingState;
	private RemoveStateEvent myRemoveEvent;

	public Automaton(FormalDefinitionComponent ... comps) {
		super(comps); 
	}

	public InputAlphabet getInputAlphabet(){
		return getComponentOfClass(InputAlphabet.class);
	}
	
	public TransitionSet<T> getTransitions(){
		return getComponentOfClass(TransitionSet.class);
	}

	public State getStartState() {
		return getComponentOfClass(StartState.class).toStateObject();
	}
	
	public void setStartState(State s){
		if (!this.getStates().contains(s)){
			throw new FormalDefinitionException("The start state must already be " +
					"a part of the state set of the automaton");
		}
		else
			getComponentOfClass(StartState.class).setTo(s);
	}

	
	public StateSet getStates() {
		return getComponentOfClass(StateSet.class);
		}

	@Override
	public void componentChanged(ChangeEvent event) {
		StateSet states = this.getStates();
		
		if (amRemovingState && event.getType() == ITEM_REMOVED){
			myRemoveEvent.addSubEvents((IUndoRedo) event);
		}else if (event.comesFrom(states) && event.getType() == STATE_REMOVED){
			amRemovingState = true;
			myRemoveEvent = ((RemoveStateEvent) event);
			State s = myRemoveEvent.getStateToRemove();
			doStateRemoval(s);
			amRemovingState = false;
			distributeChange(myRemoveEvent);
			myRemoveEvent = null;
		}
		else super.componentChanged(event);
	}

	protected void doStateRemoval(State s) {
		TransitionSet<T> transSet = this.getTransitions();
		transSet.removeForState(s);
	}
	
	
	

}
