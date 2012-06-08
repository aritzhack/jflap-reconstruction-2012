package file.xml.formaldef.components.specific.alphabet;

import model.automata.InputAlphabet;
import model.formaldef.components.SetComponent;
import model.formaldef.components.symbols.Symbol;

public class InputAlphabetTransducer extends AlphabetTransducer{

	private static final String INPUT = "input";

	@Override
	public InputAlphabet createEmptyComponent() {
		return new InputAlphabet();
	}

	@Override
	public String getAlphabetSpecificTag() {
		return INPUT;
	}

}
