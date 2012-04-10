package view.formaldef.componentpanel.alphabets;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.symbols.Symbol;
import view.formaldef.componentpanel.DefinitionComponentPanel;

public class AlphabetBar<T extends Alphabet> extends DefinitionComponentPanel<T> {

	public AlphabetBar(T comp, boolean editable) {
		super(comp, editable);
	}

	@Override
	public void updateParts() {
		// TODO Auto-generated method stub

	}

	
	public boolean highlightSymbols(Symbol ... symbols){
		clearHighlights();
		//highlight all boxes with symbols as above
		return true;
	}

	private void clearHighlights() {
		//loop through all symbol boxes, de-highlight
	}
	
	
}
