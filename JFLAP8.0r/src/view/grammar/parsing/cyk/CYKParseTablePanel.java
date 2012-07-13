package view.grammar.parsing.cyk;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.change.events.AdvancedChangeEvent;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.view.tables.HighlightTable;
import util.view.tables.SelectingEditor;
import view.grammar.parsing.RunningView;

@SuppressWarnings("serial")
public class CYKParseTablePanel extends RunningView {

	private HighlightTable myTable;
	private DefaultTableCellRenderer myRenderer;
	private SelectingEditor myEditor;

	public CYKParseTablePanel(Parser parser) {
		super("CYK Parse Table", parser);
		setLayout(new BorderLayout());

		CYKParseModel myModel = new CYKParseModel((CYKParser) parser);
		myTable = new HighlightTable(myModel);
		JScrollPane panel = new JScrollPane(myTable);
		
		myRenderer = new EmptySetCellRenderer();
		myEditor = new EmptySetCellEditor();
		
		add(panel, BorderLayout.CENTER);
	}

	private class CYKParseModel extends AbstractTableModel implements
			ChangeListener {
		private CYKParser myParser;

		public CYKParseModel(CYKParser parser) {
			myParser = parser;
			parser.addListener(this);
		}

		@Override
		public int getColumnCount() {
			SymbolString input = myParser.getInput();
			return input == null ? 0 : input.size();
		}

		@Override
		public int getRowCount() {
			return getColumnCount();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return myParser.isCellEditable(rowIndex, columnIndex);
		}

		@Override
		public Set<Symbol> getValueAt(int rowIndex, int columnIndex) {
			return myParser.getValueAt(rowIndex, columnIndex);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			Set<Symbol> attemptSet;
			if (aValue instanceof String) {
				attemptSet = new HashSet<Symbol>(Symbolizers.symbolize(
						(String) aValue, myParser.getGrammar()));
			} else
				attemptSet = (Set<Symbol>) aValue;
			if (!myParser.insertSet(rowIndex, columnIndex, attemptSet)){
				myTable.highlight(rowIndex, columnIndex);
				myTable.clearSelection();
			}
			else
				myTable.dehighlight(rowIndex, columnIndex);
		}

		private Symbol[] getColumnNames() {
			SymbolString input = myParser.getInput();
			if (input == null)
				return new Symbol[0];
			return input.toArray(new Symbol[0]);
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e instanceof AdvancedChangeEvent
					&& ((AdvancedChangeEvent) e).comesFrom(myParser)) {
				fireTableDataChanged();
				myTable.createDefaultColumnsFromModel();

				for (int i = 0; i < getColumnCount(); i++) {
					TableColumn col = myTable.getColumnModel().getColumn(i);

					col.setCellRenderer(myRenderer);
					col.setCellEditor(myEditor);
					col.setHeaderValue(getColumnNames()[i]);
					for(int j = 0; j < getColumnCount(); j++){
						if(!isCellEditable(i, j)) myTable.dehighlight(i, j);
					}
				}
			}
		}
	}

	/**
	 * The modified table cell renderer. Removes square brackets when selected,
	 * and renders empty sets as the Empty Set Symbol.
	 */
	private class EmptySetCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel l = (JLabel) super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);
			if (value == null){
				if(table.isCellEditable(row, column)) l.setText("[]");
				return l;
			}
			if (hasFocus && table.isCellEditable(row, column)) {
				l.setText(replaceSetCharacters(l.getText()));
				return l;
			}
			if (!value.equals(new HashSet<Symbol>()))
				return l;
			l.setText(JFLAPConstants.EMPTY_SET_SYMBOL.getString());
			return l;
		}
	}

	public int getSelectedRow(){
		return myTable.getSelectedRow();
	}
	
	public int getSelectedColumn(){
		return myTable.getSelectedColumn();
	}
	
	private String replaceSetCharacters(String labelText){
		String replacement = labelText.replaceAll("\\[", "");
		replacement = replacement.replaceAll("\\]", "");
		return replacement;
	}
	
	
	@Override
	public void setMagnification(double mag) {
		super.setMagnification(mag);
		float size = (float) (mag * JFLAPPreferences.getDefaultTextSize());
		myTable.setFont(this.getFont().deriveFont(size));
		myTable.setRowHeight((int) (size + 10));
	}
}
