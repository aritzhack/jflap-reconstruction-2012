package oldnewstuff.view.formaldef.componentpanel;

import gui.undo.UndoKeeper;
import oldnewstuff.view.formaldef.FormalDefinitionView;
import oldnewstuff.view.formaldef.componentpanel.alphabets.AlphabetBar;
import oldnewstuff.view.formaldef.componentpanel.symbols.SpecialSymbolPanel;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.SpecialSymbol;

public class ComponentPanelFactory {

	public static DefinitionComponentPanel createForComponent(
			FormalDefinitionComponent comp, 
			UndoKeeper keeper,
			boolean editable) {
		if (comp instanceof Alphabet){
			return new AlphabetBar((Alphabet) comp,keeper, editable);
		}
		else if (comp instanceof SpecialSymbol){
			return new SpecialSymbolPanel((SpecialSymbol) comp, keeper, editable);
		}
		return null;
	}

}
