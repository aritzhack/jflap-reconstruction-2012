package test;

import java.util.Arrays;

import util.UtilFunctions;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.formaldef.components.functionset.FunctionSet;

public class FSATest {

	
	public static void main(String[] args) {
		StateSet states = new StateSet();
		InputAlphabet input = new InputAlphabet();
		TransitionFunctionSet transitions = new TransitionFunctionSet();
		StartState start = new StartState();
		FinalStateSet finalStates = new FinalStateSet();
		
		FiniteStateAcceptor fsa = new FiniteStateAcceptor(states, 
															input, 
															transitions, 
															start, 
															finalStates);
		System.out.println(fsa);
		System.err.println(UtilFunctions.createDelimitedString(Arrays.asList(fsa.isComplete()),"\n"));
	}
}
