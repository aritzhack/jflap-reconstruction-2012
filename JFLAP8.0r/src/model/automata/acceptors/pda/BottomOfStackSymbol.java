package model.automata.acceptors.pda;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.symbols.SpecialSymbol;
import model.formaldef.components.symbols.Symbol;

public class BottomOfStackSymbol extends SpecialSymbol {

	public BottomOfStackSymbol(String s) {
		super(s);
	}

	public BottomOfStackSymbol(Symbol s) {
		super(s);
	}

	public BottomOfStackSymbol() {
		super();
	}

	@Override
	public Character getCharacterAbbr() {
		return 'z';
	}

	@Override
	public String getDescription() {
		return "The symbol at the bottom of the stack!";
	}

	@Override
	public String getDescriptionName() {
		return "Bottom of Stack Symbol";
	}

	
	
}
