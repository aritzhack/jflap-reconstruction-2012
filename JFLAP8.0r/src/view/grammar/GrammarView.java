package view.grammar;

import javax.swing.JComponent;
import javax.swing.JLabel;

import model.formaldef.FormalDefinition;
import model.grammar.Grammar;
import model.undo.UndoKeeper;
import view.formaldef.FormalDefinitionView;

public class GrammarView extends FormalDefinitionView<Grammar> {

	public GrammarView(Grammar definition, UndoKeeper keeper,
			boolean editable) {
		super(definition, keeper, editable);
	}

	@Override
	public JComponent createCentralPanel(Grammar definition,
			UndoKeeper keeper) {
		
		JLabel label = new JLabel("GRAMMAR");
		
		return label;
	}

}
