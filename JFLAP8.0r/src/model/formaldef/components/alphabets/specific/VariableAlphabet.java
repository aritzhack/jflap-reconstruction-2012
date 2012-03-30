package model.formaldef.components.alphabets.specific;

import errors.BooleanWrapper;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.Terminal;
import model.formaldef.components.alphabets.symbols.Variable;
import model.formaldef.rules.AlphabetRule;
import model.formaldef.rules.GroupingRule;



public class VariableAlphabet extends Alphabet{


	@Override
	public String getDescriptionName() {
		return "Variables";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'V';
	}

	@Override
	public String getDescription() {
		return "The Variable alphabet.";
	}

	@Override
	public String getSymbolName() {
		return "Variable";
	}

	@Override
	public boolean add(Symbol e) {
		return super.add(new Variable(e.getString()));
	}

}
