package model.automata.turing;

import java.util.Collection;

import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.Transition;
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

public abstract class TuringMachine<T extends Transition<T>> extends Acceptor<T> {


	private BlankSymbol myBlank;

	public TuringMachine(StateSet states,
							TapeAlphabet tapeAlph,
							BlankSymbol blank,
							InputAlphabet inputAlph,
							TransitionSet<T> functions,
							StartState start,
							FinalStateSet finalStates) {
		super(states, tapeAlph, blank, inputAlph, functions, start, finalStates);
		setBlankSymbol(blank);
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


	@Override
	public InputAlphabet getInputAlphabet() {
		return super.getInputAlphabet();
	}


}
