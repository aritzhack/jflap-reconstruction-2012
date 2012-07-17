package view.grammar.parsing.cyk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.change.events.AdvancedChangeEvent;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.view.tables.SelectingEditor;
import view.grammar.parsing.RunningView;

/**
 * Running View for the CYK Parsing algorithm. Interactive table used to
 * represent the CYK algorithm, which automatically moves to the next diagonal
 * when the current diagonal is complete.
 * 
 * @author Ian McMahon
 * 
 */
@SuppressWarnings("serial")
public class CYKParseTablePanel extends RunningView {

	private JTable myTable;
	private SelectingEditor myEditor;
	private CYKParser myParser;
	private Map<Integer, Boolean> myHighlightData;

	public CYKParseTablePanel(Parser parser) {
		super("CYK Parse Table", parser);
		myParser = (CYKParser) parser;
		setLayout(new BorderLayout());

		CYKParseModel myModel = new CYKParseModel(myParser);
		myTable = new JTable(myModel);
		JScrollPane panel = new JScrollPane(myTable);

		myEditor = new SetCellEditor();
		
		myHighlightData = new HashMap<Integer, Boolean>();

		add(panel, BorderLayout.CENTER);
	}

	/**
	 * Private table model that listens to the CYK parser, allows for user
	 * input, and changes accordingly.
	 */
	private class CYKParseModel extends AbstractTableModel implements
			ChangeListener {

		public CYKParseModel(CYKParser parser) {
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

		/**
		 * Only called on user input, creates a set of Symbols from the String
		 * that the user enters into the specified cell, sets the cell at that
		 * value, and highlights the cell if incorrect (and clears highlighting
		 * if correct)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			Set<Symbol> attemptSet = new HashSet<Symbol>(Symbolizers.symbolize(
					(String) aValue, myParser.getGrammar()));

			if (!myParser.insertSet(rowIndex, columnIndex, attemptSet)) {
				myHighlightData.put(singleIndex(rowIndex, columnIndex), true);
				myTable.clearSelection();
			} else
				myHighlightData.put(singleIndex(rowIndex, columnIndex), false);
		}

		/**
		 * Stores the input as a Symbol[] to be accessed as column header names
		 */
		private HighlightHeader[] getColumnNames() {
			SymbolString input = myParser.getInput();
			if (input == null)
				return new HighlightHeader[0];
			HighlightHeader[] columns = new HighlightHeader[input.size()];
			for (int i = 0; i < columns.length; i++) {
				columns[i] = new HighlightHeader(input.get(i));
			}
			return columns;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e instanceof AdvancedChangeEvent
					&& ((AdvancedChangeEvent) e).comesFrom(myParser)) {
				notifyTable();

				for (int i = 0; i < getColumnCount(); i++) {
					TableColumn col = myTable.getColumnModel().getColumn(i);

					col.setCellRenderer(new EmptySetCellRenderer());
					col.setHeaderRenderer(new HighlightTableHeaderRenderer());
					col.setCellEditor(myEditor);
					col.setHeaderValue(getColumnNames()[i]);

					for (int j = 0; j < getColumnCount(); j++) {
						if(!myHighlightData.containsKey(singleIndex(i, j)))
							myHighlightData.put(singleIndex(i, j), false);
						if (!isCellEditable(i, j))
							myHighlightData.put(singleIndex(i, j), false);
					}
				}
			}
		}

		private void notifyTable() {
			fireTableDataChanged();
			myTable.createDefaultColumnsFromModel();
			JTableHeader header = myTable.getTableHeader();

			header.setReorderingAllowed(false);
			header.setResizingAllowed(false);
		}
	}

	/**
	 * Autofills the selected cell, if it is editable.
	 */
	public void doSelected() {
		int row = myTable.getSelectedRow();
		int col = myTable.getSelectedColumn();
		myParser.autofillCell(row, col);
		myHighlightData.put(singleIndex(row, col), false);
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
			if (hasFocus) {
				dehighlightHeaders();
				if (table.isCellEditable(row, column))
					highlightHeader(row, column);
			}
			if (myHighlightData.get(singleIndex(row, column))){
				l.setBackground(new Color(255, 150, 150));
			} else{
				l.setBackground(Color.white);
			}
			if (value == null) {
				if (table.isCellEditable(row, column))
					l.setText("[]");
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

	private class HighlightTableHeaderRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel head = (JLabel) super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);
			if (value instanceof HighlightHeader) {
				HighlightHeader headerValue = (HighlightHeader) value;
				if (headerValue.getHighlight() != null) {
					this.setForeground(headerValue.getHighlight());
				}
			}
			head.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			head.setOpaque(false);
			return head;
		}
	}

	private void highlightHeader(int start, int end) {
		for (int i = start; i <= end; i++) {
			HighlightHeader header = (HighlightHeader) myTable.getColumnModel()
					.getColumn(i).getHeaderValue();
			header.setHightlight(Color.red);
		}
		repaint();

	}

	private void dehighlightHeaders() {
		for (int i = 0; i < myTable.getColumnCount(); i++) {
			HighlightHeader header = (HighlightHeader) myTable.getColumnModel()
					.getColumn(i).getHeaderValue();
			header.setHightlight(Color.black);
		}
		repaint();
	}

	/**
	 * Helper method to aid in the rendering/editing of sets so that square
	 * brackets are not counted as input.
	 */
	private String replaceSetCharacters(String labelText) {
		String replacement = labelText.replaceAll("\\[", "");
		replacement = replacement.replaceAll("\\]", "");
		return replacement;
	}
	
	/**
	 * Converts a row and column index to an index.
	 */
	private static int singleIndex(int row, int column) {
		return row + (column << 22);
	}

	@Override
	public void setMagnification(double mag) {
		super.setMagnification(mag);
		float size = (float) (mag * JFLAPPreferences.getDefaultTextSize());
		Font font = this.getFont().deriveFont(size);
		myTable.setFont(font);
		myTable.getTableHeader().setFont(font);
		myTable.setRowHeight((int) (size + 10));
	}
}
