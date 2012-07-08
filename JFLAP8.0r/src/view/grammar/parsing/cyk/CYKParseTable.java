package view.grammar.parsing.cyk;

import java.awt.Color;
import java.awt.Component;
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import model.grammar.Grammar;
import model.grammar.parsing.cyk.CYKParser;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.view.magnify.Magnifiable;
import util.view.tables.HighlightTable;

/**
 * Highlighting, magnifying table for the construction and visualization of CYK
 * Parse Table specific to the current input and grammar
 * 
 * @author Ian McMahon
 * 
 */
public class CYKParseTable extends HighlightTable implements Magnifiable {
	private final TableCellRenderer RENDERER = new EmptySetCellRenderer();

	public CYKParseTable(Grammar gram) {
		super(new CYKParseModel(new CYKParser(gram)));
	}

	/**
	 * Resets the target string to be parsed to input. Reinitializes the table
	 * with a new, corresponding table model.
	 * 
	 * @param input
	 *            the new target to be parsed.
	 */
	public void setInput(SymbolString input) {
		CYKParseModel model = (CYKParseModel) getModel();

		setModel(new CYKParseModel(model.getParser(), input));
		model = (CYKParseModel) getModel();
		model.setEditableRow(0);

		for (int i = 0; i < input.size(); i++) {
			TableColumn col = getColumnModel().getColumn(i);

			col.setCellRenderer(RENDERER);
			col.setCellEditor(new EmptySetCellEditor());
			col.setHeaderValue(model.getColumnNames()[i]);
		}
		setCellSelectionEnabled(true);
	}

	/** The built in highlight renderer generator. */
	private static final HighlightTable.TableHighlighterRendererGenerator THRG = new TableHighlighterRendererGenerator() {
		public TableCellRenderer getRenderer(int row, int column) {
			if (renderer == null) {
				renderer = new DefaultTableCellRenderer();
				renderer.setBackground(new Color(255, 150, 150));
			}
			return renderer;
		}

		private DefaultTableCellRenderer renderer = null;
	};

	/** Modified to use the set renderer highlighter. */
	public void highlight(int row, int column) {
		highlight(row, column, THRG);
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
			if (value == null)
				return l;
			if (hasFocus && table.isCellEditable(row, column)) {
				l.setText(l.getText().replaceAll("\\[", ""));
				l.setText(l.getText().replaceAll("\\]", ""));
				return l;
			}
			if (!value.equals(new HashSet<Symbol>()))
				return l;
			l.setText(JFLAPConstants.EMPTY_SET_SYMBOL.getString());
			return l;
		}
	}

	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag * JFLAPPreferences.getDefaultTextSize());
		this.setFont(this.getFont().deriveFont(size));
		this.setRowHeight((int) (size + 10));
	}
}
