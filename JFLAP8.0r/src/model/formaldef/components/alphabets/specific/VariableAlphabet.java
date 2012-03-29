package model.formaldef.components.alphabets.specific;

import errors.BooleanWrapper;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.Terminal;
import model.formaldef.components.alphabets.symbols.Variable;



public class VariableAlphabet extends Alphabet{


	@Override
	public String getName() {
		return "Variables";
	}

	@Override
	public Character getCharacterAbbr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
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
