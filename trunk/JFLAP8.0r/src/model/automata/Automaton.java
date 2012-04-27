package model.automata;

import java.util.Set;

import model.formaldef.FormalDefinition;
import model.formaldef.components.ComponentChangeEvent;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.symbols.Symbol;
import model.util.UtilFunctions;

public abstract class Automaton<T extends Transition> extends FormalDefinition{

	public Automaton(FormalDefinitionComponent ... comps) {
		super(comps); 
	}

	public InputAlphabet getInputAlphabet(){
		return getComponentOfClass(InputAlphabet.class);
	}
	
	
	public TransitionFunctionSet<T> getTransitions(){
		return getComponentOfClass(TransitionFunctionSet.class);
	}

	public StartState getStartState() {
		return getComponentOfClass(StartState.class);
		}

	
	public StateSet getStates() {
		return getComponentOfClass(StateSet.class);
		}

	@Override
	public void componentChanged(ComponentChangeEvent event) {
		StateSet states = this.getStates();
		
		if (event.comesFrom(states) && event.getType() == ITEM_REMOVED){
			TransitionFunctionSet<T> transSet = this.getTransitions();
			State s = (State) event.getArg(0);
			Set<T> from = transSet.getTransitionsFromState(s);
			Set<T> to = transSet.getTransitionsToState(s);
			transSet.removeAll(from);
			transSet.removeAll(to);
		}
		else if (event.comesFrom(getInputAlphabet()) && event.getType() == ITEM_REMOVED){
			this.getTransitions().purgeofInputSymbol((Symbol) event.getArg(0));
		}
		else super.componentChanged(event);
	}
	
	
	

}
