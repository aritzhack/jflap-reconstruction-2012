package model.automata.turing.buildingblock;

import java.util.Collection;

import model.automata.SingleInputTransition;
import model.automata.State;
import model.automata.Transition;
import model.formaldef.components.SetSubComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class BuildingBlockTransition extends SingleInputTransition<BuildingBlockTransition> {
	
	public BuildingBlockTransition(BuildingBlock from, BuildingBlock to, SymbolString input){
		super(from, to, input);
	}

	@Override
	public String getDescriptionName() {
		return "Building Block Transition";
	}

	@Override
	public String getDescription() {
		return "The transition for a Building Block Turing Machine";
	}
	
	@Override
	public BuildingBlock getFromState(){
		return (BuildingBlock) super.getFromState();
	}
	
	@Override
	public BuildingBlock getToState(){
		return (BuildingBlock) super.getToState();
	}

	@Override
	public BuildingBlockTransition copy() {
		return new BuildingBlockTransition(this.getFromState(), this.getToState(), new SymbolString(this.getInput()));
	}

}
