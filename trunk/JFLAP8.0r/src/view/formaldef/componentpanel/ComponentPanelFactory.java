package view.formaldef.componentpanel;

import view.formaldef.componentpanel.alphabets.AlphabetBar;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.undo.UndoKeeper;

public class ComponentPanelFactory {

	public static DefinitionComponentPanel createForComponent(FormalDefinitionComponent comp,
																UndoKeeper keeper, boolean editable) {
		if (comp instanceof Alphabet)
			return new AlphabetBar<Alphabet>((Alphabet) comp, keeper, editable);
		return null;
	}

}
