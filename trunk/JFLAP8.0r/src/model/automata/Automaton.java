package model.automata;

import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.FunctionSet;
import model.util.UtilFunctions;

public abstract class Automaton <T extends Transition> extends FormalDefinition<InputAlphabet, TransitionFunctionSet<T>> {

	private StateSet myStates;
	private StartState myStartState;

	public Automaton(StateSet states,
						InputAlphabet langAlph, 
						TransitionFunctionSet<T> functions,
						StartState start) {
		super(langAlph, functions);
		myStates = states;
		myStartState = start;
	}

	public InputAlphabet getInputAlphabet(){
		return this.getLanguageAlphabet();
	}
	
	
	public TransitionFunctionSet<T> getTransitions(){
		return this.getFunctionSet();
	}

	@Override
	public FormalDefinitionComponent[] getComponents() {
		return new FormalDefinitionComponent[]{this.getStates(),
											this.getInputAlphabet(),
											this.getTransitions(),
											this.getStartState()};
	}

	public StartState getStartState() {
		return myStartState;
	}

	
	public StateSet getStates() {
		return myStates;
	}
	
	
	

}
