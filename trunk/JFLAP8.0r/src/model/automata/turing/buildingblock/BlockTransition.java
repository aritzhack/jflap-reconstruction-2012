package model.automata.turing.buildingblock;

import java.util.Collection;

import model.automata.SingleInputTransition;
import model.automata.State;
import model.automata.Transition;
import model.formaldef.components.SetSubComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class BlockTransition extends SingleInputTransition<BlockTransition> {
	
	public BlockTransition(Block from, Block to, SymbolString input){
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
	public Block getFromState(){
		return (Block) super.getFromState();
	}
	
	@Override
	public Block getToState(){
		return (Block) super.getToState();
	}

	@Override
	public BlockTransition copy() {
		return new BlockTransition(this.getFromState(), this.getToState(), new SymbolString(this.getInput()));
	}

}
