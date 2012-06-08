package file.xml.formaldef.components.specific.alphabet;

import model.automata.InputAlphabet;
import model.formaldef.components.SetComponent;
import model.formaldef.components.symbols.Symbol;

public class InputAlphabetTransducer extends AlphabetTransducer{


	@Override
	public InputAlphabet createEmptyComponent() {
		return new InputAlphabet();
	}

	@Override
	public String getTypeTag() {
		return INPUT_ALPH;
	}


}
