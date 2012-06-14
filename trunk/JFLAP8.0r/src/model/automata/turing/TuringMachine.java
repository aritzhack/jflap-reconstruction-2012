package model.automata.turing;

import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.formaldef.FormalDefinition;
import model.formaldef.components.ComponentChangeEvent;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.rules.applied.TuringMachineBlankRule;
import model.formaldef.rules.applied.TuringMachineRule;

public class TuringMachine extends Acceptor<TuringMachineTransition> {


	private BlankSymbol myBlank;



	public TuringMachine(StateSet states,
							TapeAlphabet tapeAlph,
							BlankSymbol blank,
							TuringMachineInputAlphabet inputAlph,
							TransitionSet<TuringMachineTransition> functions,
							StartState start,
							FinalStateSet finalStates) {
		super(states, tapeAlph, blank, inputAlph, functions, start, finalStates);
		setBlankSymbol(blank);

		this.getInputAlphabet().addRules(new TuringMachineRule(this));

	}
	


	@Override
	public String getDescriptionName() {
		return "Turing Machine (TM)";
	}

	@Override
	public String getDescription() {
		return "A turing machine!";
	}

	@Override
	public TuringMachine alphabetAloneCopy() {
		return new TuringMachine(new StateSet(),
									this.getTapeAlphabet(), 
									(BlankSymbol) myBlank.copy(), 
									(TuringMachineInputAlphabet) this.getInputAlphabet(), 
									new TransitionSet<TuringMachineTransition>(), 
									new StartState(), 
									new FinalStateSet());
	}



	public Symbol getBlankSymbol() {
		return getComponentOfClass(BlankSymbol.class).toSymbolObject();
	}



	private void setBlankSymbol(BlankSymbol blank) {
		myBlank = blank;
		this.getTapeAlphabet().add(blank.toSymbolObject());
		this.getTapeAlphabet().addRules(new TuringMachineBlankRule(myBlank));
	}


	public TapeAlphabet getTapeAlphabet() {
		return getComponentOfClass(TapeAlphabet.class);
	}



	@Override
	public void componentChanged(ComponentChangeEvent event) {
		if (event.comesFrom(getTapeAlphabet()) && event.getType() == ITEM_REMOVED){
			InputAlphabet input = this.getInputAlphabet();
			Symbol s = (Symbol) event.getArg(0);
			if (input.contains(s))
				input.remove(s);
		}
		super.componentChanged(event);
	}



	public int getNumTapes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TuringMachineInputAlphabet getInputAlphabet() {
		return (TuringMachineInputAlphabet) super.getInputAlphabet();
	}

	@Override
	public TuringMachine copy() {
		return new TuringMachine(this.getStates().copy(),
				this.getTapeAlphabet().copy(),
				myBlank.copy(),
				this.getInputAlphabet().copy(), 
				this.getTransitions().copy(), 
				new StartState(this.getStartState().copy()), 
				this.getFinalStateSet().copy());
	}


	
	

}
