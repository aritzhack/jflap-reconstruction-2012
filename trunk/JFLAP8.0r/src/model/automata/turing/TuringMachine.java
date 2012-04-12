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
import model.formaldef.components.symbols.Symbol;
import model.formaldef.rules.applied.TuringMachineRule;

public class TuringMachine extends Acceptor<TuringMachineTransition> {


	private BlankSymbol myBlank;



	public TuringMachine(StateSet states,
							TapeAlphabet tapeAlph,
							BlankSymbol blank,
							TuringMachineInputAlphabet inputAlph,
							TransitionFunctionSet<TuringMachineTransition> functions,
							StartState start,
							FinalStateSet finalStates) {
		super(states, tapeAlph, blank, inputAlph, functions, start, finalStates);
		myBlank = blank;
		this.getInputAlphabet().addRules(new TuringMachineRule(this));
		setBlankSymbol(blank.toSymbolObject());
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
									new TransitionFunctionSet<TuringMachineTransition>(), 
									new StartState(), 
									new FinalStateSet());
	}



	public Symbol getBlankSymbol() {
		return getComponentOfClass(BlankSymbol.class).toSymbolObject();
	}



	public void setBlankSymbol(Symbol blank) {
		this.getTapeAlphabet().remove(getBlankSymbol());
		this.myBlank.setTo(blank);
		this.getTapeAlphabet().add(blank);
	}


	public TapeAlphabet getTapeAlphabet() {
		return getComponentOfClass(TapeAlphabet.class);
	}


}
