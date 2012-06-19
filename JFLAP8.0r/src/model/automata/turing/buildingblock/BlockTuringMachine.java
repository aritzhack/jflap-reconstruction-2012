package model.automata.turing.buildingblock;

import java.util.Collection;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.TransitionSet;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.change.events.AdvancedChangeEvent;
import model.formaldef.FormalDefinition;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.rules.applied.TuringMachineBlankRule;

public class BlockTuringMachine extends Acceptor<BlockTransition> {

	private BlankSymbol myBlank;
	
	public BlockTuringMachine(BlockSet states, TapeAlphabet tapeAlph,
			BlankSymbol blank, InputAlphabet inputAlph,
			TransitionSet<BlockTransition> functions, StartState start,
			FinalStateSet finalStates) {
		super(states, tapeAlph, blank, inputAlph, functions, start, finalStates);
		setBlankSymbol(blank);
	}

	private void setBlankSymbol(BlankSymbol blank) {
		myBlank = blank;
		this.getTapeAlphabet().add(blank.getSymbol());
		this.getTapeAlphabet().addRules(new TuringMachineBlankRule(myBlank));
	}
	
	@Override
	public BlockSet getStates(){
		return (BlockSet) super.getStates();
	}

	@Override
	public String getDescriptionName() {
		return "A Block Turing Machine";
	}

	@Override
	public String getDescription() {
		return "Turing machine utilizing building blocks";
	}

	@Override
	public BlockTuringMachine copy() {
		return new BlockTuringMachine(this.getStates().copy(),
				this.getTapeAlphabet().copy(),
				myBlank.copy(),
				this.getInputAlphabet().copy(), 
				this.getTransitions().copy(), 
				new StartState(this.getStartState().copy()), 
				this.getFinalStateSet().copy());
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
	public FormalDefinition alphabetAloneCopy() {
		return new BlockTuringMachine(new BlockSet(),
				this.getTapeAlphabet(), 
				myBlank.copy(), 
				this.getInputAlphabet(), 
				new TransitionSet<BlockTransition>(), 
				new StartState(), 
				new FinalStateSet());
	}

}
