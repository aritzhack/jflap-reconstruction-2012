package file.xml.formaldef.components.specific.alphabet;

import model.formaldef.components.SetComponent;
import model.formaldef.components.symbols.Symbol;
import model.grammar.VariableAlphabet;

public class VariablesTransducer extends AlphabetTransducer {


	@Override
	public String getTag() {
		return VARIABLES_TAG;
	}

	@Override
	public SetComponent<Symbol> createEmptyComponent() {
		return new VariableAlphabet();
	}

}
