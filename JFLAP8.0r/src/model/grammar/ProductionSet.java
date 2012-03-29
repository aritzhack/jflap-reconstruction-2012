package model.grammar;

import model.formaldef.components.functionset.FunctionSet;

public class ProductionSet extends FunctionSet<Production> {

	@Override
	public Character getCharacterAbbr() {
		return 'P';
	}

	@Override
	public String getDescriptionName() {
		return "Production Rules";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
