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
import javax.swing.table.TableColumnModel;

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
import util.view.DoSelectable;
import util.view.tables.SelectingEditor;
import view.grammar.parsing.RunningView;

/**
 * Running View/interactive table used to
 * represent the CYK algorithm. Allows for user input, which is compared to 
 * the true values, which are precalculated one step ahead. Also allows for
 * autofilling of single cells, rows, or entire table as well as animation
 * as to which pairs of cells need to be considered to fill in selected cell
 * with correct variables.
 * 
 * @author Ian McMahon
 * 
 */
@SuppressWarnings("serial")
public class CYKParseTablePanel extends RunningView implements DoSelectable{

	private static final Color YELLOW_HIGHLIGHT = new Color(255, 255, 66);
	private static final Color RED_HIGHLIGHT = new Color(255, 150, 150);
	private static final Color TRANSPARENT = JFLAPUniverse
			.getActiveEnvironment().getBackground();

	private SelectingEditor myEditor;
	private Map<Integer, Color> myHighlightData;
	private EmptySetCellRenderer myRenderer;
	private HighlightTableHeaderRenderer myHeadRenderer;
	private boolean diagonal;
	private boolean animated;

	public CYKParseTablePanel(Parser parser, boolean diagonal) {
		super("CYK Parse Table", parser);
		this.diagonal = diagonal;
		
		JTable table = getTable();
		table.setGridColor(TRANSPARENT);

		myEditor = new SetCellEditor();
		myRenderer = new EmptySetCellRenderer();
		myHeadRenderer = new HighlightTableHeaderRenderer();

		myHighlightData = new HashMap<Integer, Color>();

	}

	@Override
	public AbstractTableModel createModel(Parser parser) {
		return new CYKParseModel((CYKParser) parser);
	}

	/**
	 * Private table model that listens to the CYK parser, allows for user
	 * input, and changes accordingly.
	 */
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
				setCellColor(rowIndex, columnIndex, RED_HIGHLIGHT);
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
				
				TableColumnModel columnModel = getTable().getColumnModel();
				
				for (int i = 0; i < getColumnCount(); i++) {
					setTableColumnInfo(columnModel, i);

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
		private void setTableColumnInfo(TableColumnModel model, int i) {
			TableColumn column = model.getColumn(i);

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
			getTable().createDefaultColumnsFromModel();
		}
		
		private CYKParser getParser(){
			return (CYKParser) myParser;
		}
	}

	/**
	 * Autofills the selected cell, if it is editable.
	 */
	@Override
	public void doSelected() {
		JTable table = getTable();
		CYKParser parser = ((CYKParseModel) table.getModel()).getParser();
		
		int row = table.getSelectedRow();
		int column = table.getSelectedColumn();
		parser.autofillCell(getRowFromMapping(row, column), column);
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
					l.setText("{}");
				return l;
			}
			if (!value.equals(new HashSet<Symbol>())){
				replaceSetCharacters(l);
				return l;
			}
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
		JTable table = getTable();
		
		int row = table.getSelectedRow();
		int column = table.getSelectedColumn();
		
		if (animated || !table.isCellEditable(row, column))
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
			setCellColor(mappedRow, k, YELLOW_HIGHLIGHT);
			setCellColor(mappedK, column, YELLOW_HIGHLIGHT);
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
		TableColumnModel columnModel = getTable().getColumnModel();
		
		for (int i = row; i <= column; i++) {
			HighlightHeader header = (HighlightHeader) columnModel
					.getColumn(i).getHeaderValue();
			header.setHightlight(YELLOW_HIGHLIGHT);
		}
	}

	/**
	 * Sets all headers to be de-highlighted on the next repaint.
	 */
	private void dehighlightHeaders() {
		JTable table = getTable();
		TableColumnModel columnModel = table.getColumnModel();
		
		for (int i = 0; i < table.getColumnCount(); i++) {
			HighlightHeader header = (HighlightHeader) columnModel
					.getColumn(i).getHeaderValue();
			header.setHightlight(TRANSPARENT);
		}
	}

	/**
	 * Helper method to aid in the display of set cells so that square
	 * brackets are rendered as {} to fit set notation.
	 */
	private void replaceSetCharacters(JLabel label) {

		String replacement = label.getText().replaceAll("\\[", "{");
		replacement = replacement.replaceAll("\\]", "}");
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
}
