package model.automata.simulate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import model.automata.Automaton;
import model.automata.StartState;
import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.formaldef.FormalDefinition;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class PDASimulator {

	
	private PushdownAutomaton myAutomaton;

	public PDASimulator (PushdownAutomaton a){
		myAutomaton = a;
	}
	
	
	public boolean acceptsInput(String in){
		SymbolString input = new SymbolString();
		for (String s: in.split("\\s+")){
			input.addAll(SymbolString.createFromString(s, this.getAutomaton().getInputAlphabet()));
		}
		return testInput(input, 
				this.getAutomaton().getStartState().toStateObject(), 
				new SymbolString(this.getAutomaton().getBottomOfStackSymbol()));
		
		
	}


	private boolean testInput(SymbolString input, 
								State curr,
								SymbolString stack) {
//		System.out.println("In: " + input);
//		System.out.println("State: " + curr);
//		System.out.println("Stack: " + stack + "\n");

		
		PDATransition[] trans = this.getNextTransitions(input, curr, stack);
//		System.out.println(Arrays.toString(trans));
		SymbolString subInput = input.isEmpty() ? input : input.subList(1);
		
		for (PDATransition t : trans){
			SymbolString temp = new SymbolString(stack);
			for (int i = 0; i < t.getPop().size(); i++){
				temp.pop();
			}
			temp.addAll(0, t.getPush());
			if (testInput(subInput, t.getToState(), temp)){
				return true;
			}
		}
		
		
		return input.isEmpty() && this.getAutomaton().getFinalStateSet().contains(curr);
	}


	private PDATransition[] getNextTransitions(SymbolString input, 
													State curr,
													SymbolString stack) {
		
		ArrayList<PDATransition> trans = new ArrayList<PDATransition>();
		
		for (PDATransition t: this.getAutomaton().getTransitions()){
			if(t.getFromState().equals(curr) &&
					input.startsWith(t.getInput()) &&
					stack.startsWith(t.getPop())){
				trans.add(t);
			}
		}
		
		
		return trans.toArray(new PDATransition[0]);
	}



	private PushdownAutomaton getAutomaton() {
		return myAutomaton;
	}
	
}
