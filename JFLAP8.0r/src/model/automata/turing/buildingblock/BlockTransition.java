package model.automata.turing.buildingblock;

import java.util.Collection;
import java.util.Set;

import preferences.JFLAPPreferences;

import util.JFLAPConstants;

import model.automata.AutomatonException;
import model.automata.SingleInputTransition;
import model.automata.State;
import model.automata.Transition;
import model.formaldef.components.SetSubComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class BlockTransition extends SingleInputTransition<BlockTransition> implements JFLAPConstants {
	
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
	
	@Override
	public boolean setInput(SymbolString input) {
		if (input.isEmpty())
			input.add(JFLAPPreferences.getTMBlankSymbol());
		
		int i = 0;
		
		if (input.startsWith(NOT)) i = 1;
		
		SymbolString symbol = input.subList(i);
		
		if (symbol.size() > 1)
			throw new AutomatonException("You may not set the input on a TM block transition " +
					"to a string of my than one symbols.");
		
		return super.setInput(input);
	}
	
	@Override
	public boolean setTo(BlockTransition other) {
		return super.setTo(other);
	}
	
	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		Set<Symbol> symbols = super.getSymbolsUsedForAlphabet(a);
		symbols.remove(TILDE);
		symbols.remove(NOT);
		return symbols;
	}
}
