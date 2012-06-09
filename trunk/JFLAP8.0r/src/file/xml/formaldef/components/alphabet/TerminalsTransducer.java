package file.xml.formaldef.components.alphabet;

import model.formaldef.components.SetComponent;
import model.formaldef.components.symbols.Symbol;
import model.grammar.TerminalAlphabet;

public class TerminalsTransducer extends AlphabetTransducer {


	@Override
	public String getTag() {
		return TERMINALS_TAG;
	}

	@Override
	public SetComponent<Symbol> createEmptyComponent() {
		return new TerminalAlphabet();
	}

}
