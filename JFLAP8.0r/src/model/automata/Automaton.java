package model.automata;

import model.formaldef.FormalDefinition;
import model.formaldef.FormalDefinitionException;
import model.formaldef.components.ComponentChangeEvent;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.symbols.Symbol;

public abstract class Automaton<T extends Transition<T>> extends FormalDefinition{

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
	public void componentChanged(ComponentChangeEvent event) {
		StateSet states = this.getStates();
		
		if (event.comesFrom(states) && event.getType() == ITEM_REMOVED){
			TransitionSet<T> transSet = this.getTransitions();
			State s = (State) event.getArg(0);
			transSet.removeForState(s);
		}
		else if (event.comesFrom(getInputAlphabet()) && event.getType() == ITEM_REMOVED){
			this.getTransitions().purgeOfSymbol(getInputAlphabet(),((Symbol) event.getArg(0)));
		}
		else super.componentChanged(event);
	}
	
	
	

}
