package model.grammar;

import errors.BooleanWrapper;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
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
		if (!(e instanceof Variable))
			e = new Variable(e.getString());
		return super.add(e);
	}

	@Override
	public VariableAlphabet copy() {
		return (VariableAlphabet) super.copy();
	}
}
