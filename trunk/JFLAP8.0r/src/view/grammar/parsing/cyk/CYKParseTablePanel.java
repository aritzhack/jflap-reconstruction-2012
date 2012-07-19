package view.grammar.parsing.cyk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import debug.JFLAPDebug;

import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.change.events.AdvancedChangeEvent;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import universe.JFLAPUniverse;
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

	private static final Color HEADER_HIGHLIGHT = new Color(255, 215, 0);
	private static final Color TABLE_HIGHLIGHT = new Color(255, 150, 150);
	private static final Color TRANSPARENT = JFLAPUniverse
			.getActiveEnvironment().getBackground();

	private JTable myTable;
	private SelectingEditor myEditor;
	private CYKParser myParser;
	private Map<Integer, Color> myHighlightData;
	private EmptySetCellRenderer myRenderer;
	private HighlightTableHeaderRenderer myHeadRenderer;
	private boolean diagonal;
	private boolean animated;

	public CYKParseTablePanel(Parser parser, boolean diagonal) {
		super("CYK Parse Table", parser);
		myParser = (CYKParser) parser;
		this.diagonal = diagonal;
		setLayout(new BorderLayout());

		CYKParseModel model = new CYKParseModel(myParser);
		myTable = new JTable(model);
		JScrollPane panel = new JScrollPane(myTable);

		JTableHeader header = myTable.getTableHeader();
		header.setReorderingAllowed(false);
		header.setResizingAllowed(false);
		myTable.setGridColor(TRANSPARENT);

		myEditor = new SetCellEditor();
		myRenderer = new EmptySetCellRenderer();
		myHeadRenderer = new HighlightTableHeaderRenderer();

		myHighlightData = new HashMap<Integer, Color>();

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
			return myParser.isCellEditable(
					getRowFromMapping(rowIndex, columnIndex), columnIndex);
		}

		@Override
		public Set<Symbol> getValueAt(int rowIndex, int columnIndex) {
			return myParser.getValueAt(
					getRowFromMapping(rowIndex, columnIndex), columnIndex);
		}

		/**
		 * Only called on user input, creates a set of Symbols from the String
		 * that the user enters into the specified cell, sets the cell at that
		 * value, and highlights the cell if incorrect (and clears highlighting
		 * if correct)
		 */
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			String value = (String) aValue;
			value = value.replaceAll(" ", "");
			Set<Symbol> attemptSet = new HashSet<Symbol>(Symbolizers.symbolize(
					value, myParser.getGrammar()));
			int row = getRowFromMapping(rowIndex, columnIndex);

			if (!myParser.insertSet(row, columnIndex, attemptSet))
				setCellColor(rowIndex, columnIndex, TABLE_HIGHLIGHT);
			else
				setCellColor(rowIndex, columnIndex, Color.WHITE);
		}

		/**
		 * Stores the input as a HighlightHeader[] to be accessed as column
		 * header names with the correctly colored background.
		 */
		private HighlightHeader[] getColumnNames() {
			SymbolString input = myParser.getInput();
			if (input == null)
				return new HighlightHeader[0];
			HighlightHeader[] columns = new HighlightHeader[input.size()];
			for (int i = 0; i < columns.length; i++) {
				columns[i] = new HighlightHeader(input.get(i));
				columns[i].setHightlight(TRANSPARENT);
			}
			return columns;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e instanceof AdvancedChangeEvent
					&& ((AdvancedChangeEvent) e).comesFrom(myParser)) {
				notifyTable();

				for (int i = 0; i < getColumnCount(); i++) {
					setTableColumnInfo(i);

					for (int j = i; j < getColumnCount(); j++) {
						if (!myHighlightData.containsKey(singleIndex(i, j))
								|| !isCellEditable(i, j))
							setCellColor(i, j, Color.WHITE);
					}
				}
			}
		}

		/**
		 * Sets the TableColumn cellRenderer, headerRenderer, cellEditor, and
		 * headerValue.
		 */
		private void setTableColumnInfo(int i) {
			TableColumn column = myTable.getColumnModel().getColumn(i);

			column.setCellRenderer(myRenderer);
			column.setHeaderRenderer(myHeadRenderer);
			column.setCellEditor(myEditor);
			column.setHeaderValue(getColumnNames()[i]);
		}

		/**
		 * Updates the table when the model is changed.
		 */
		private void notifyTable() {
			fireTableDataChanged();
			myTable.createDefaultColumnsFromModel();
		}
	}

	/**
	 * Autofills the selected cell, if it is editable.
	 */
	public void doSelected() {
		int row = myTable.getSelectedRow();
		int column = myTable.getSelectedColumn();
		myParser.autofillCell(getRowFromMapping(row, column), column);
		setCellColor(row, column, Color.WHITE);
	}

	/**
	 * Sets the background of the cell specified by (row, column)
	 */
	private void setCellColor(int row, int column, Color color) {
		myHighlightData.put(singleIndex(row, column), color);
	}

	/**
	 * Returns the background of the cell at (row, column)
	 */
	private Color getCellColor(int row, int column) {
		return myHighlightData.get(singleIndex(row, column));
	}

	/**
	 * The modified table cell renderer. Removes square brackets when selected,
	 * renders empty sets as the Empty Set Symbol, and deals with highlighting
	 * of table cells (notifying the header renderer when necessary).
	 */
	private class EmptySetCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel l = (JLabel) super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);
			if (hasFocus) {
				dehighlightHeaders();
				if (table.isCellEditable(row, column) && isSelected)
					highlightHeader(row, column);
				CYKParseTablePanel.this.repaint();
			}
			setCellBackground(row, column, l);

			if (value == null) {
				if (table.isCellEditable(row, column))
					l.setText("[]");
				return l;
			}
			if (hasFocus && table.isCellEditable(row, column)) {
				replaceSetCharacters(l);
				return l;
			}
			if (!value.equals(new HashSet<Symbol>()))
				return l;
			l.setText(JFLAPConstants.EMPTY_SET_SYMBOL.getString());
			return l;
		}
	}

	/**
	 * Highlights and dehighlights valid cells, while rendering any unusable
	 * cells as completely transparent.
	 */
	private void setCellBackground(int row, int column, JLabel l) {
		if (row > column) {
			l.setBackground(TRANSPARENT);
			l.setBorder(BorderFactory.createEmptyBorder());
			return;
		}
		l.setBackground(getCellColor(row, column));
	}

	/**
	 * Highlights the cells that must be paired for creating the set of
	 * variables that belong in the selected cell.
	 */
	public void animate() {
		int row = myTable.getSelectedRow();
		int column = myTable.getSelectedColumn();
		
		if (animated || !myTable.isCellEditable(row, column))
			return;

		row = getRowFromMapping(row, column);
		if (row < column) {
			animated = true;
			HighlightAction animate = new HighlightAction(row, column);
		}
		repaint();
	}

	private class HighlightAction implements ActionListener {

		private int row;
		private int column;
		private int k;
		private Timer timer;

		public HighlightAction(int row, int column) {
			this.row = row;
			this.k = row;
			this.column = column;

			this.timer = new Timer(1000, this);
			timer.setInitialDelay(500);
			timer.start();
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			int mappedRow = getRowFromMapping(row, k);
			int mappedK = getRowFromMapping(k + 1, column);
			int oldRow = getRowFromMapping(row, k - 1);
			int oldK = getRowFromMapping(k, column);

			if (k != row) {
				setCellColor(oldRow, k - 1, Color.white);
				setCellColor(oldK, column, Color.white);
				CYKParseTablePanel.this.repaint();
				if (k >= column) {
					timer.stop();
					animated = false;
					// Get rid of this listener, no longer needed.
					try {
						this.finalize();
					} catch (Throwable e) {
						e.printStackTrace();
					}
					return;
				}
			}
			setCellColor(mappedRow, k, HEADER_HIGHLIGHT);
			setCellColor(mappedK, column, HEADER_HIGHLIGHT);
			CYKParseTablePanel.this.repaint();
			k++;
		}
	}

	/**
	 * Renderer used for the headers of the CYK Table
	 */
	private class HighlightTableHeaderRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel header = (JLabel) super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);
			if (value instanceof HighlightHeader) {
				HighlightHeader headerValue = (HighlightHeader) value;
				if (headerValue.getHighlight() != null) {
					this.setBackground(headerValue.getHighlight());
				}
			}
			header.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			header.setHorizontalAlignment(SwingConstants.CENTER);
			return header;
		}
	}

	/**
	 * Sets the headers that correspond to the selected cell to be highlighted
	 * on the next repaint.
	 */
	private void highlightHeader(int row, int column) {
		row = getRowFromMapping(row, column);
		for (int i = row; i <= column; i++) {
			HighlightHeader header = (HighlightHeader) myTable.getColumnModel()
					.getColumn(i).getHeaderValue();
			header.setHightlight(HEADER_HIGHLIGHT);
		}
	}

	/**
	 * Sets all headers to be de-highlighted on the next repaint.
	 */
	private void dehighlightHeaders() {
		for (int i = 0; i < myTable.getColumnCount(); i++) {
			HighlightHeader header = (HighlightHeader) myTable.getColumnModel()
					.getColumn(i).getHeaderValue();
			header.setHightlight(TRANSPARENT);
		}
	}

	/**
	 * Helper method to aid in the rendering/editing of sets so that square
	 * brackets are not counted as input.
	 */
	private void replaceSetCharacters(JLabel label) {

		String replacement = label.getText().replaceAll("\\[", "");
		replacement = replacement.replaceAll("\\]", "");
		label.setText(replacement);
	}

	/**
	 * Converts a row and column index to an index.
	 */
	private int singleIndex(int row, int column) {
		return row + (column << 22);
	}

	/**
	 * Depending on <CODE>diagonal</CODE> value, will return row itself or
	 * column - row, so that the table will appear either diagonal or
	 * horizontal, respectively, without having to modify CYK Parser.
	 */
	private int getRowFromMapping(int row, int column) {
		return (diagonal || row > column) ? row : column - row;
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
