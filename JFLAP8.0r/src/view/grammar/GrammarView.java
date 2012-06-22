package view.grammar;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import model.formaldef.FormalDefinition;
import model.grammar.Grammar;
import model.undo.UndoKeeper;
import view.formaldef.FormalDefinitionView;

public class GrammarView extends FormalDefinitionView<Grammar> {

	private ProductionTable myTable;

	public GrammarView(Grammar definition, UndoKeeper keeper,
			boolean editable) {
		super(definition, keeper, editable);
	}

	@Override
	public JComponent createCentralPanel(Grammar definition,
			UndoKeeper keeper, boolean editable) {

		return new ProductionTable(definition.getProductionSet(), 
										keeper, 
										editable);
	}

}