package model.automata.acceptors;

import javax.swing.event.ChangeEvent;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.formaldef.FormalDefinition;
import model.formaldef.FormalDefinitionException;
import model.formaldef.components.ComponentChangeEvent;
import model.formaldef.components.FormalDefinitionComponent;

public abstract class Acceptor<T extends Transition> extends Automaton<T> {


	public Acceptor(FormalDefinitionComponent ...comps) {
		super(comps);
	}
	
	public FinalStateSet getFinalStateSet(){
		return getComponentOfClass(FinalStateSet.class);
	}
	
	public static boolean isFinalState(Acceptor a, State s){
		return ((Acceptor) a).getFinalStateSet().contains(s);
	}
	
	@Override
	public void componentChanged(ComponentChangeEvent event) {
		FinalStateSet finalStates = getFinalStateSet();
		if (event.comesFrom(finalStates) &&
				event.getType() == ITEM_ADDED){
			State added = (State) event.getArg(0);
			if(!this.getStates().contains(added)){
				finalStates.remove(added);
				throw new FormalDefinitionException("A final state must first be a part " +
						"of the state set of the accepter.");
			}
				
		}
		super.componentChanged(event);
	}
}
