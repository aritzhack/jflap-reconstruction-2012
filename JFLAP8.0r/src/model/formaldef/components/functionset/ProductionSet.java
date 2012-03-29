package model.formaldef.components.functionset;

import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.functionset.function.Production;

public class ProductionSet extends FunctionSet<Production> {

	@Override
	public Character getCharacterAbbr() {
		return 'P';
	}

	@Override
	public String getName() {
		return "Production Rules";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
