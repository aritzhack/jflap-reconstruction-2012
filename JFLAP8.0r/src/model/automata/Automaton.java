package model.automata;

import java.util.Collection;

import model.change.events.AdvancedChangeEvent;
import model.formaldef.FormalDefinition;
import model.formaldef.FormalDefinitionException;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.symbols.Symbol;

public abstract class Automaton<T extends Transition<T>> extends FormalDefinition{

	public Automaton(FormalDefinitionComponent ... comps) {
		super(comps); 
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
		
		if (event.comesFrom(StateSet.class) && event.getType() == ITEM_REMOVED){
			TransitionSet<T> transSet = this.getTransitions();
			Collection<State> s = (Collection<State>) event.getArg(0);
			transSet.removeForStates(s);
		}
		else if(event.comesFrom(StartState.class) && event.getType() == SPECIAL_CHANGED){
			this.getStates().add(this.getStartState());
		}
		else super.componentChanged(event);
	}
	
	
	

}
