package view.grammar.parsing.cyk;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import model.algorithms.testinput.InputUsingAlgorithm;
import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.change.events.AdvancedChangeEvent;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import util.JFLAPConstants;
import util.view.DoSelectable;
import util.view.tables.SelectingEditor;
import view.grammar.parsing.RunningView;

/**
 * Running View/interactive table used to represent the CYK algorithm. Allows
 * for user input, which is compared to the true values, which are precalculated
 * one step ahead. Also allows for autofilling of single cells, rows, or entire
 * table as well as animation as to which pairs of cells need to be considered
 * to fill in selected cell with correct variables.
 * 
 * @author Ian McMahon
 * 
 */
// JZG - Organize your methods in the class. Put all inner-classes at the bottom
// in a block together,
// put private methods together, put inherited methods together, and put public
// methods together.
// I tend to order my classes:
// Constants
// Fields
// Constructors
// Overridden Methods
// Public Methods
// Private Methods
// Inner classes
@SuppressWarnings("serial")
public class CYKParseTablePanel extends RunningView implements DoSelectable {

	private static final Color YELLOW_HIGHLIGHT = new Color(255, 255, 66);
	private static final Color RED_HIGHLIGHT = new Color(255, 150, 150);

	private SelectingEditor myEditor;
	private Map<Integer, Color> myHighlightData;
	private EmptySetCellRenderer myRenderer;
	private HighlightTableHeaderRenderer myHeadRenderer;
	private boolean diagonal;
	private Timer animationTimer;

	public CYKParseTablePanel(Parser parser, boolean diagonal) {
		super("CYK Parse Table", parser);
		this.diagonal = diagonal;

		JTable table = getTable();
		table.setGridColor(JFLAPConstants.DEFAULT_SWING_BG);

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
	 * Autofills the selected cell, if it is editable, making sure there is a
	 * selected cell.
	 */
	@Override
	public void doSelected() {
		JTable table = getTable();
		CYKParser parser = ((CYKParseModel) table.getModel()).getParser();

		int row = table.getSelectedRow();
		int column = table.getSelectedColumn();
		if (row < 0 || column < 0)
			return;

		int newColumn = getColumnFromParser(row, column);
		int newRow = getRowFromParser(row, column);

		parser.autofillCell(newRow, newColumn);
		setCellColor(row, column, Color.WHITE);
	}

	/**
	 * Highlights the cells that must be paired for creating the set of
	 * variables that belong in the selected cell.
	 */
	public void animate() {
		JTable table = getTable();

		int row = table.getSelectedRow();
		int column = table.getSelectedColumn();

		if ((animationTimer != null && animationTimer.isRunning())
				|| !table.isCellEditable(row, column))
			return;
		int newColumn = getColumnFromParser(row, column);
		int newRow = getRowFromParser(row, column);

		if (newRow < newColumn) {
			animationTimer = new Timer(1000, new HighlightAction(newRow,
					newColumn));
			animationTimer.setInitialDelay(500);
			animationTimer.start();
		} else
			dehighlightHeaders();
		table.clearSelection();
		repaint();
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
	 * Highlights and dehighlights valid cells, while rendering any unusable
	 * cells as transparent.
	 */
	private void setCellBackground(int row, int column, JLabel l) {
		if ((diagonal && row > column)
				|| (!diagonal && row + column >= getTable().getColumnCount())) {
			l.setBackground(JFLAPConstants.DEFAULT_SWING_BG);
			l.setBorder(BorderFactory.createEmptyBorder());
			return;
		}
		l.setBackground(getCellColor(row, column));
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
		int newColumn = getColumnFromParser(row, column);
		int newRow = getRowFromParser(row, column);

		TableColumnModel columnModel = getTable().getColumnModel();

		for (int i = newRow; i <= newColumn; i++) {
			HighlightHeader header = (HighlightHeader) columnModel.getColumn(i)
					.getHeaderValue();
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
			HighlightHeader header = (HighlightHeader) columnModel.getColumn(i)
					.getHeaderValue();
			header.setHightlight(JFLAPConstants.DEFAULT_SWING_BG);
		}
	}

	/**
	 * Helper method to aid in the display of set cells so that square brackets
	 * are rendered as {} to fit set notation.
	 */
	private void replaceSetCharacters(JLabel label) {

		String replacement = label.getText().replaceAll("\\[", "{");
		replacement = replacement.replaceAll("\\]", "}");
		label.setText(replacement);
	}

	/**
	 * Converts a row and column index to an index. //JZG - Huh? The comment
	 * could be clearer
	 */
	private int singleIndex(int row, int column) {
		return row + (column << 22);
	}

	/**
	 * Returns the row in the CYK Parser that is represented by row, column in
	 * the table. If the table is diagonal, it is simply the row, if not, it is
	 * the column.
	 */
	private int getRowFromParser(int row, int column) {
		return diagonal ? row : column;
	}

	/**
	 * Returns the column in the CYK Parser that is represented by row, column
	 * in the table. If the table is diagonal, it is simply the column, if not,
	 * it is the the column plus the row (wrapping around input's length).
	 */
	private int getColumnFromParser(int row, int column) {
		if (diagonal)
			return column;
		int newCol = column + row;
		int length = getTable().getColumnCount();

		return newCol < length ? newCol : newCol - length;
	}

	/**
	 * Returns the row in the table that is represented by row, column in the
	 * CYK Parser. If diagonal, it is simply the row, otherwise it is the column
	 * - the row (wrapping around the input's length)
	 */
	private int getRowFromTable(int row, int column) {
		// JZG - so lets say I am someone trying to use JFLAP to build soemthing
		// cool.
		// I am familiar with the interface, and I love how you can toggle
		// between
		// the diagonal rows vs. normal rows in the CYK parse table. It is my
		// dream to
		// make a program that prints out a CYK parse table in a loop,
		// alternating between diagonal and non-diagonal. But LO I cannot
		// because this
		// is only functionality present in the view. I am a sad panda :(
		// TL;DR, I think you should put the diagonal boolean in the model
		// component
		// and make it do all the hard work translating when I say
		// getValue(row,column)
		// That way you dont have to do all this weird and repetitive
		// getRow/ColumnFromTable/Parser

		if (diagonal)
			return row;
		int newRow = column - row;
		int length = getTable().getColumnCount();

		return newRow >= 0 ? newRow : newRow + length;
	}

	/**
	 * Returns the column in the table that is represented by row, column in the
	 * CYK Parser. If diagonal, it is simply the column, otherwise it is the
	 * row.
	 */
	private int getColumnFromTable(int row, int column) {
		return diagonal ? column : row;
	}

	/**
	 * Private table model that listens to the CYK parser, allows for user
	 * input, and changes accordingly.
	 */
	private class CYKParseModel extends AbstractTableModel implements
			ChangeListener {
		private CYKParser myParser;
		private HighlightHeader[] myHeaders;

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
			int newColumn = getColumnFromParser(rowIndex, columnIndex);
			int newRow = getRowFromParser(rowIndex, columnIndex);

			return myParser.isActive(newRow, newColumn);
		}

		@Override
		public Set<Symbol> getValueAt(int rowIndex, int columnIndex) {
			int newColumn = getColumnFromParser(rowIndex, columnIndex);
			int newRow = getRowFromParser(rowIndex, columnIndex);

			return myParser.getValueAt(newRow, newColumn);
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

			Set<Symbol> attemptSet = new HashSet<Symbol>(Symbolizers.symbolize(
					value, myParser.getGrammar()));
			int newColumn = getColumnFromParser(rowIndex, columnIndex);
			int newRow = getRowFromParser(rowIndex, columnIndex);

			boolean inserted = myParser.insertSet(newRow, newColumn,
					attemptSet); // JZG - I refactored this to save you time.

			setCellColor(rowIndex, columnIndex, inserted ? Color.WHITE
					: RED_HIGHLIGHT);
		}
		
		@Override
		public void stateChanged(ChangeEvent e) {
			if (e instanceof AdvancedChangeEvent
					&& ((AdvancedChangeEvent) e).comesFrom(myParser)) {
				if (((AdvancedChangeEvent) e).getType() == InputUsingAlgorithm.INPUT_SET) {
					myHighlightData.clear();
					createColumnHeaders();
				}

				notifyTable();
				notifyAnimationTimer();

				TableColumnModel columnModel = getTable().getColumnModel();

				for (int i = 0; i < getColumnCount(); i++) {
					setTableColumnInfo(columnModel, i);

					for (int j = diagonal ? i : 0; j < (diagonal ? getColumnCount()
							: getColumnCount() - i); j++) {
						if (!myHighlightData.containsKey(singleIndex(i, j))
								|| !isCellEditable(i, j))

							setCellColor(i, j, Color.WHITE);
					}
				}
			}
		}

		/**
		 * Stores the input as a HighlightHeader[] to be accessed as column
		 * header names with the correctly colored background.
		 */
		private void createColumnHeaders() {
			SymbolString input = myParser.getInput();
			if (input == null){
				myHeaders = new HighlightHeader[0];
				return;
			}
			
			myHeaders = new HighlightHeader[input.size()];
			for (int i = 0; i < myHeaders.length; i++) {
				myHeaders[i] = new HighlightHeader(input.get(i));
				myHeaders[i].setHightlight(JFLAPConstants.DEFAULT_SWING_BG);
			}
		}

		/**
		 * Stops animation when a cell, row, or table is autofilled, or if the
		 * table is reset, so there aren't cells being highlighted at random.
		 */
		private void notifyAnimationTimer() {
			if (animationTimer != null && animationTimer.isRunning()) {
				animationTimer.stop();
				animationTimer = null;
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
			column.setHeaderValue(myHeaders[i]);
		}

		/**
		 * Updates the table when the model is changed.
		 */
		private void notifyTable() {
			fireTableDataChanged();
			getTable().createDefaultColumnsFromModel();
		}

		private CYKParser getParser() {
			return (CYKParser) myParser;
		}
	}

	/**
	 * The modified table cell renderer. Replaces square brackets with {},
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
			if (!value.equals(new HashSet<Symbol>())) {
				replaceSetCharacters(l);
				return l;
			}
			l.setText(JFLAPConstants.EMPTY_SET_SYMBOL.getString());
			return l;
		}
	}

	private class HighlightAction implements ActionListener {

		private int row;
		private int column;
		private int k;

		public HighlightAction(int row, int column) {
			this.row = row;
			this.k = row;
			this.column = column;
		}

		@Override
		public void actionPerformed(ActionEvent event) {

			if (k != row) {
				setCellColor(getRowFromTable(row, k - 1),
						getColumnFromTable(row, k - 1), Color.white);
				setCellColor(getRowFromTable(k, column),
						getColumnFromTable(k, column), Color.white);
				CYKParseTablePanel.this.repaint();
				if (k >= column) {
					animationTimer.stop();
					dehighlightHeaders();
					// Get rid of this listener, no longer needed.
					try {
						this.finalize();
					} catch (Throwable e) {
						e.printStackTrace();
					}
					return;
				}
			}
			setCellColor(getRowFromTable(row, k), getColumnFromTable(row, k),
					YELLOW_HIGHLIGHT);
			setCellColor(getRowFromTable(k + 1, column),
					getColumnFromTable(k + 1, column), YELLOW_HIGHLIGHT);
			CYKParseTablePanel.this.repaint();
			k++;
		}
	}

}
