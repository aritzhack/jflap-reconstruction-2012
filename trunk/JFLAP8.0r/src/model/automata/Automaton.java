package model.automata;

import util.UtilFunctions;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.specific.InputAlphabet;
import model.formaldef.components.functionset.FunctionSet;

public abstract class Automaton extends FormalDefinition<InputAlphabet, TransitionFunctionSet> {

	private StateSet myStates;
	private StartState myStartState;

	public Automaton(StateSet states,
						InputAlphabet langAlph, 
					TransitionFunctionSet functions,
					StartState start) {
		super(langAlph, functions);
		myStates = states;
		myStartState = start;
	}

	public InputAlphabet getInputAlphabet(){
		return this.getLanguageAlphabet();
	}
	
	
	public TransitionFunctionSet getTransitions(){
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
