package model.automata.turing;

import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionFunctionSet;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.rules.TuringMachineRule;

public class TuringMachine extends Acceptor<TuringMachineTransition> {


	private BlankSymbol myBlank;
	private TapeAlphabet myTapeAlphabet;

	public TuringMachine(StateSet states,
							TapeAlphabet tapeAlph,
							BlankSymbol blank,
							TuringMachineInputAlphabet langAlph,
							TransitionFunctionSet<TuringMachineTransition> functions,
							StartState start,
							FinalStateSet finalStates) {
		super(states, langAlph, functions, start, finalStates);
		
		setBlankSymbol(blank);
		myTapeAlphabet = tapeAlph;
		this.getInputAlphabet().addRules(new TuringMachineRule(this));
	}
	


	@Override
	public String getDescriptionName() {
		return "Turing Machine (TM)";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TuringMachine alphabetAloneCopy() {
		return new TuringMachine(new StateSet(),
									this.getTapeAlphabet(), 
									new BlankSymbol(), 
									(TuringMachineInputAlphabet) this.getInputAlphabet(), 
									new TransitionFunctionSet<TuringMachineTransition>(), 
									new StartState(), 
									new FinalStateSet());
	}



	public BlankSymbol getBlankSymbol() {
		return myBlank;
	}



	public void setBlankSymbol(BlankSymbol blank) {
		this.myBlank = blank;
	}


	public TapeAlphabet getTapeAlphabet() {
		return myTapeAlphabet;
	}



	@Override
	public FormalDefinitionComponent[] getComponents() {
		return new FormalDefinitionComponent[]{this.getStates(),
													this.getTapeAlphabet(),
													this.getBlankSymbol(),
													this.getInputAlphabet(),
													this.getTransitions(),
													this.getStartState(),
													this.getFinalStateSet()};
	}
	
	


}
