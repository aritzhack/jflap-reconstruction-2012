package model.automata.turing.buildingblock;

import java.util.Collection;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.TransitionSet;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.change.events.AdvancedChangeEvent;
import model.formaldef.FormalDefinition;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.rules.applied.TuringMachineBlankRule;

public class BlockTuringMachine extends TuringMachine<BlockTransition> {

	public BlockTuringMachine(BlockSet states, 
								TapeAlphabet tapeAlph,
								BlankSymbol blank, 
								InputAlphabet inputAlph,
								TransitionSet<BlockTransition> functions, 
								StartState start,
								FinalStateSet finalStates) {
		super(states, tapeAlph, blank, inputAlph, functions, start, finalStates);
	}

	@Override
	public BlockSet getStates(){
		return (BlockSet) super.getStates();
	}

	@Override
	public String getDescriptionName() {
		return "Block Turing Machine";
	}

	@Override
	public String getDescription() {
		return "Turing machine utilizing building blocks";
	}
	
	@Override
	public void setStartState(State s) {
		super.setStartState((Block)s);
	}

	@Override
	public Block getStartState() {
		return (Block) super.getStartState();
	}
	
	@Override
	public BlockTuringMachine copy() {
		return new BlockTuringMachine(this.getStates().copy(),
				this.getTapeAlphabet().copy(),
				new BlankSymbol(),
				this.getInputAlphabet().copy(), 
				this.getTransitions().copy(), 
				new StartState(this.getStartState().copy()), 
				this.getFinalStateSet().copy());
	}
	
	
	@Override
	public FormalDefinition alphabetAloneCopy() {
		return new BlockTuringMachine(new BlockSet(),
				this.getTapeAlphabet(), 
				new BlankSymbol(),
				this.getInputAlphabet(), 
				new TransitionSet<BlockTransition>(), 
				new StartState(), 
				new FinalStateSet());
	}

}
