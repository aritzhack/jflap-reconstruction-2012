package model.formaldef.components.symbols;

public class BottomOfStackSymbol extends SpecialSymbol {

	public BottomOfStackSymbol(String s) {
		super(s);
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
	public String getName() {
		return "Bottom of Stack Symbol";
	}

}
