package test;

import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAutomaton;
import model.formaldef.components.alphabets.specific.InputAlphabet;
import model.formaldef.components.functionset.FunctionSet;

public class FSATest {

	
	public static void main(String[] args) {
		StateSet states = new StateSet();
		InputAlphabet input = new InputAlphabet();
		TransitionFunctionSet transitions = new TransitionFunctionSet();
		StartState start = new StartState();
		FinalStateSet finalStates = new FinalStateSet();
		
		FiniteStateAutomaton fsa = new FiniteStateAutomaton(states, 
															input, 
															transitions, 
															start, 
															finalStates);
		System.out.println(fsa);
	}
}
