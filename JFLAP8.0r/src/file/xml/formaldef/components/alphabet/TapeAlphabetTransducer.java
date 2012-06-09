package file.xml.formaldef.components.alphabet;

import model.formaldef.components.SetComponent;
import model.formaldef.components.symbols.Symbol;

public class TapeAlphabetTransducer extends AlphabetTransducer {

	@Override
	public String getTag() {
		return TAPE_ALPH;
	}

	@Override
	public SetComponent<Symbol> createEmptyComponent() {
		// TODO Auto-generated method stub
		return null;
	}

}
