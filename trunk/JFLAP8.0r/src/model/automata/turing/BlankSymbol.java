package model.automata.turing;

import preferences.JFLAPPreferences;
import errors.BooleanWrapper;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.symbols.SpecialSymbol;
import model.formaldef.components.symbols.Symbol;

public class BlankSymbol extends SpecialSymbol {

	public BlankSymbol() {
		super(JFLAPPreferences.getTMBlankSymbol());
	}
	
	private BlankSymbol(Symbol s){
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
		return "The blank symbol used to represent the empty strin on a " +
				"Turing Machine tape.";
	}
	
	@Override
	public void setTo(Symbol s) {
		//do nothing;
	}
	
	@Override
	public BlankSymbol copy() {
		return new BlankSymbol(this.toSymbolObject());
	}

}
