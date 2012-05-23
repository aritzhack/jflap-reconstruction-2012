package model.automata.turing;

import errors.BooleanWrapper;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.symbols.SpecialSymbol;
import model.formaldef.components.symbols.Symbol;

public class BlankSymbol extends SpecialSymbol {

	public BlankSymbol(String s) {
		super(s);
	}

	@Override
	public Character getCharacterAbbr() {
		return 'b';
	}

	@Override
	public String getDescriptionName() {
		return "Turing Machine Blank Symbol";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}