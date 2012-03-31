package model.formaldef.components.symbols;

public class BottomOfStackSymbol extends SpecialSymbol {

	public BottomOfStackSymbol(String s) {
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
