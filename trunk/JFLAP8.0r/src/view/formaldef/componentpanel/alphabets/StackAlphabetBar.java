package view.formaldef.componentpanel.alphabets;

import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.formaldef.components.alphabets.Alphabet;

public class StackAlphabetBar extends AlphabetBar {

	public StackAlphabetBar(Alphabet comp, BottomOfStackSymbol bos, boolean editable) {
		super(comp, editable);
		this.setBottomOfStackSymbol(bos);
	}

	public void setBottomOfStackSymbol(BottomOfStackSymbol bos) {
		super.highlightSymbols(bos);
	}

}
