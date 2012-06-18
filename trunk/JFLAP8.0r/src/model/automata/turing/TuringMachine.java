package model.automata.turing;

import java.util.Collection;

import oldnewstuff.TuringMachineInputAlphabet;
import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.change.events.AdvancedChangeEvent;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.rules.applied.TuringMachineBlankRule;
import model.formaldef.rules.applied.TuringMachineRule;

public class TuringMachine extends Acceptor<TuringMachineTransition> {


	private BlankSymbol myBlank;
	private int myNumTapes;

	public TuringMachine(StateSet states,
							TapeAlphabet tapeAlph,
							BlankSymbol blank,
							InputAlphabet inputAlph,
							TransitionSet<TuringMachineTransition> functions,
							StartState start,
							FinalStateSet finalStates, 
							int numTapes) {
		super(states, tapeAlph, blank, inputAlph, functions, start, finalStates);
		myNumTapes = numTapes;
		setBlankSymbol(blank);
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
									this.getInputAlphabet(), 
									new TransitionSet<TuringMachineTransition>(), 
									new StartState(), 
									new FinalStateSet(),
									myNumTapes);
	}



	public Symbol getBlankSymbol() {
		return getComponentOfClass(BlankSymbol.class).getSymbol();
	}



	private void setBlankSymbol(BlankSymbol blank) {
		myBlank = blank;
		this.getTapeAlphabet().add(blank.getSymbol());
		this.getTapeAlphabet().addRules(new TuringMachineBlankRule(myBlank));
	}


	public TapeAlphabet getTapeAlphabet() {
		return getComponentOfClass(TapeAlphabet.class);
	}



	@Override
	public void componentChanged(AdvancedChangeEvent event) {
		if (event.comesFrom(TapeAlphabet.class) && event.getType() == ITEM_REMOVED){
			InputAlphabet input = this.getInputAlphabet();
			Collection<Symbol> s = (Collection<Symbol>) event.getArg(0);
			input.removeAll(s);
		} else if (event.comesFrom(InputAlphabet.class) && event.getType() == ITEM_ADDED){
			TapeAlphabet tape = this.getTapeAlphabet();
			Collection<Symbol> s = (Collection<Symbol>) event.getArg(0);
			tape.addAll(s);
		}
		super.componentChanged(event);
	}



	public int getNumTapes() {
		return myNumTapes;
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
				this.getFinalStateSet().copy(),
				myNumTapes);
	}


	
	

}
