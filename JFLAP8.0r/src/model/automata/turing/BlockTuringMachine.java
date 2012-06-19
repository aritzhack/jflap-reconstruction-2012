package model.automata.turing;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.TransitionSet;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.formaldef.FormalDefinition;
import model.formaldef.rules.applied.TuringMachineBlankRule;

public class BlockTuringMachine extends Acceptor<BuildingBlockTransition> {

	private BlankSymbol myBlank;
	
	public BlockTuringMachine(BuildingBlockSet states, TapeAlphabet tapeAlph,
			BlankSymbol blank, InputAlphabet inputAlph,
			TransitionSet<BuildingBlockTransition> functions, StartState start,
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
	public BuildingBlockSet getStates(){
		return (BuildingBlockSet) super.getStates();
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
	public FormalDefinition alphabetAloneCopy() {
		return new BlockTuringMachine(new BuildingBlockSet(),
				this.getTapeAlphabet(), 
				myBlank.copy(), 
				this.getInputAlphabet(), 
				new TransitionSet<BuildingBlockTransition>(), 
				new StartState(), 
				new FinalStateSet());
	}

}
