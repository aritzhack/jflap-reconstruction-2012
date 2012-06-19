package model.automata.turing;

import java.util.Set;

import preferences.JFLAPPreferences;

import errors.BooleanWrapper;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
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
	public boolean setSymbol(Symbol s) {
		//do nothing
		return false;
	}

	@Override
	public BlankSymbol copy() {
		return new BlankSymbol(this.getSymbol());
	}

	public static BlankSymbol newInstance(String s) {
		return new BlankSymbol(new Symbol(s));
	}

	@Override
	public Class<? extends Alphabet> getAlphabetClass() {
		return TapeAlphabet.class;
	}
}
